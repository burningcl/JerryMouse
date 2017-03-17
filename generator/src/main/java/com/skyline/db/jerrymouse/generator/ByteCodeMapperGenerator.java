package com.skyline.db.jerrymouse.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

/**
 *
 */
public class ByteCodeMapperGenerator {

	List<String> msgList = new ArrayList<>();

	protected void log(String msg) {
		msgList.add(msg);
		System.out.println(msg);
	}

	protected void log(String msg, Exception e) {
		log(msg);
		log(e.toString());
		log(e.getMessage());
		e.printStackTrace();
	}

	protected void insertClassPath(ClassPool pool, String... classpaths) throws NotFoundException {
		if (classpaths != null && classpaths.length > 0) {
			for (String classpath : classpaths) {
				if (classpath.contains(":")) {
					this.insertClassPath(pool, classpath.split(":"));
				} else {
					log("gen, insertClassPath, classpath: " + classpath);
					pool.insertClassPath(classpath);
					this.classpaths.add(classpath);
				}
			}
		}
	}

	CtClass cursorClass;

	Class<?> dbTableClass;

	Class<?> dbFieldClass;

	CtClass baseMapperClass;

	private String targetClassPath;

	private ClassPool pool;

	List<String> classpaths;

	public List<String> gen(String targetClassPath, String... dependciesClassPaths) {
		try {
			log("gen, start");
			this.targetClassPath = targetClassPath;
			pool = ClassPool.getDefault();
			classpaths = new ArrayList<>();
			insertClassPath(pool, targetClassPath);
			insertClassPath(pool, dependciesClassPaths);

			cursorClass = pool.get("android.database.Cursor");
			log("found Cursor");

			dbTableClass = pool.get("com.skyline.db.jerrymouse.core.annotation.DbTable").toClass();
			log("found DbTable");

			dbFieldClass = pool.get("com.skyline.db.jerrymouse.core.annotation.DbField").toClass();
			log("found DbField");

			baseMapperClass = pool.get("com.skyline.db.jerrymouse.core.mapper.ormapper.AbsOrMapper");
			log("found AbsOrMapper");

			this.gen(new File(targetClassPath));

		} catch (Exception e) {
			log("gen, fail", e);
		}
		return msgList;
	}

	protected void gen(File dir) throws NotFoundException, CannotCompileException, IOException {
		if (dir == null || !dir.exists() || !dir.isDirectory()) {
			return;
		}
		File[] files = dir.listFiles();
		if (files == null || files.length <= 0) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				this.gen(file);
			} else {
				this.genMapperClass(file);
			}
		}
	}

	protected void genMapperClass(File file) throws NotFoundException, CannotCompileException, IOException {
		if (file == null || !file.exists() || !file.isFile()) {
			return;
		}
		if (!file.getName().endsWith(".class")) {
			// it is not a class file
			return;
		}

		String filename = file.getAbsolutePath();
		if (!filename.startsWith(targetClassPath)) {
			return;
		}

		// change the file path to class name
		filename = filename.substring(targetClassPath.length() + 1);
		filename = filename.replace(".class", "");
		filename = filename.replace("/", ".");
		filename = filename.replace("\\", ".");

		CtClass srcClass = pool.get(filename);

		try {
			Object dbTable = srcClass.getAnnotation(dbTableClass);
			if (dbTable == null) {
				return;
			}
		} catch (ClassNotFoundException e) {
			return;
		}

		this.genMapperClass(srcClass);
	}

	protected void genMapperClass(CtClass srcClass) throws CannotCompileException, NotFoundException, IOException {
		String className = srcClass.getName();
		String simpleName = srcClass.getSimpleName();
		log("genMapperClass, targetClass: " + className + ", " + simpleName);

		String mapperClassName = "generated.mappper." + simpleName + "Mapper";
		CtClass mapperClass;
		try {
			mapperClass = pool.get(mapperClassName);
			log("genMapperClass, targetClass: " + mapperClassName + " exists!");
		} catch (NotFoundException e) {
			mapperClass = pool.makeClass(mapperClassName);
			log("genMapperClass, targetClass: " + mapperClassName + " not found, create!");
		}
		mapperClass.getClassFile().setMajorVersion(51);
		mapperClass.setSuperclass(baseMapperClass);

		CtMethod mapperMethod;
		try {
			mapperMethod = mapperClass.getDeclaredMethod("map");
			mapperClass.removeMethod(mapperMethod);
			log("genMapperClass, map method exist, del it!");
		} catch (NotFoundException e) {
		}

		StringBuilder sb = new StringBuilder();
		sb.append("public Object map(android.database.Cursor c) throws java.lang.Exception{")
				.append("if (c == null || !c.moveToNext()) {return null;}")
				.append(className + " ret = new " + className + "();");
		//System.out.println(srcClass.getFields()[0].getType().getName());
		this.addMapMethodBody(srcClass.getFields(), sb);
		sb.append("return ret;}");

		mapperMethod = CtNewMethod.make(sb.toString(), mapperClass);
		mapperClass.addMethod(mapperMethod);
		mapperClass.writeFile(targetClassPath);

		log("genMapperClass, targetClass: " + mapperClassName + ", success!");
	}

	protected Annotation getAnnotation(FieldInfo fieldInfo) {
		List<Object> attrList = fieldInfo.getAttributes();
		if (attrList == null) {
			return null;
		}
		for (Object attr : attrList) {
			if (attr instanceof AnnotationsAttribute) {
				AnnotationsAttribute annoAttr = (AnnotationsAttribute) attr;
				Annotation annotation = annoAttr.getAnnotation("com.skyline.db.jerrymouse.core.annotation.DbField");
				if (annotation != null) {
					return annotation;
				}
			}
		}
		return null;
	}

	private String getColumnName(Annotation annotation, FieldInfo fieldInfo) {
		String fieldName = fieldInfo.getName();
		MemberValue val = annotation.getMemberValue("name");
		if (val == null) {
			return fieldName;
		}
		StringMemberValue strVal = (StringMemberValue) val;
		return strVal != null ? strVal.getValue() : fieldName;
	}

	private String getFieldMapper(Annotation annotation) throws NotFoundException, CannotCompileException {
		MemberValue val = annotation.getMemberValue("mapper");
		if (val == null) {
			return null;
		}
		ClassMemberValue classVal = (ClassMemberValue) val;
		CtClass mapperClass = pool.get(classVal.getValue());
		return mapperClass == null ? null : mapperClass.getName();
	}

	private String getColumnTypeFromMapper(Annotation annotation) throws NotFoundException {
		MemberValue val = annotation.getMemberValue("mapper");
		if (val == null) {
			return null;
		}
		ClassMemberValue classVal = (ClassMemberValue) val;
		CtClass mapperClass = pool.get(classVal.getValue());
		CtMethod[] methods = mapperClass.getMethods();
		for (CtMethod method : methods) {
			if (method.getName().equals("mapDbType")) {
				return method.getParameterTypes()[0].getName();
			}
		}
		return null;
	}

	protected String getTypeClass(String type) {
		switch (type) {
			case "long":
				return "Long.TYPE";
			case "int":
				return "Integer.TYPE";
			case "double":
				return "Double.TYPE";
			case "float":
				return "Float.TYPE";
			case "short":
				return "Short.TYPE";
			case "byte":
				return "Byte.TYPE";
			case "boolean":
				return "Boolean.TYPE";
			default:
				return type + ".class";
		}
	}

	// 此处有坑
	/**
	 *
	 * @param fieldName
	 * @param type
	 * @return
	 */
	protected String genSetValueSentence(String fieldName, String type) {
		String wrappedType = null;
		switch (type) {
			case "long":
				wrappedType = "Long";
				break;
			case "int":
				wrappedType = "Integer";
				break;
			case "double":
				wrappedType = "Double";
				break;
			case "float":
				wrappedType = "Float";
				break;
			case "short":
				wrappedType = "Short";
				break;
			case "byte":
				wrappedType = "Byte";
				break;
			case "boolean":
				wrappedType = "Boolean";
				break;
			default:
				return "ret." + fieldName + " = (" + type + ") value;";
		}
		return "ret." + fieldName + " = ((" + wrappedType + ") value)." + type + "Value();";
	}


	protected void addMapMethodBody(CtField[] fields, StringBuilder sb) throws NotFoundException, CannotCompileException {
		if (fields == null || fields.length <= 0) {
			return;
		}
		for (CtField ctField : fields) {
			FieldInfo fieldInfo = ctField.getFieldInfo();
			Annotation annotation = this.getAnnotation(fieldInfo);
			if (annotation == null) {
				continue;
			}
			String type = ctField.getType().getName();
			String columnName = this.getColumnName(annotation, fieldInfo);
			String mapperClass = this.getFieldMapper(annotation);
			String columnType = type;
			if (mapperClass != null) {
				columnType = getColumnTypeFromMapper(annotation);
			}
			log("addMapMethodBody, fieldName: " + fieldInfo.getName() + ",\tcolumnName: " + columnName + ",\tmapperClass: " + mapperClass + ",\ttype: " + type + ",\tcolumnType: " + columnType);

			sb.append("int columnIndex = c.getColumnIndex(\"" + columnName + "\");");
			sb.append("java.lang.Object value =  getFieldValue(c, columnIndex, " + getTypeClass(columnType) + ");");
			if (mapperClass != null) {
				sb.append("value =  com.skyline.db.jerrymouse.core.util.TypeMapperHelper.getInstance(" + mapperClass + ".class).mapDbType(value);");
			}
			sb.append(genSetValueSentence(fieldInfo.getName(), type));
		}
	}


//	private  Pair<Type, Type> parseMapperGenericType(Class<?  > clazz) {
//		if (clazz == null) {
//			log("clazz is null!");
//			return null;
//		}
//		Type[] interfaceTypes = clazz.getGenericInterfaces();
//		if (interfaceTypes == null || interfaceTypes.length <= 0) {
//			Type superClass = clazz.getGenericSuperclass();
//			if (superClass != null && superClass instanceof Class<?>) {
//				try {
//					return parseMapperGenericType((Class<?  >) superClass);
//				} catch (Exception ignore) {
//					return null;
//				}
//			} else {
//				return null;
//			}
//
//		}
//		String canonicalName = ITypeMapper.class.getCanonicalName();
//		if (canonicalName == null) {
//			return null;
//		} else {
//			canonicalName = canonicalName + "<";
//		}
//		ParameterizedType pt = null;
//		for (Type interfaceType : interfaceTypes) {
//			if (interfaceType == null) {
//				continue;
//			}
//			String typeStr = interfaceType.toString();
//			if (!(typeStr.startsWith(canonicalName) && typeStr.endsWith(">"))) {
//				continue;
//			}
//			if (!(interfaceType instanceof ParameterizedType)) {
//				continue;
//			}
//			pt = (ParameterizedType) interfaceType;
//			break;
//		}
//		if (pt == null) {
//			LogUtil.w(LOG_TAG, "ParameterizedType not found!");
//			return null;
//		}
//		Type[] type = pt.getActualTypeArguments();
//		if (type == null || type.length != 2) {
//			LogUtil.w(LOG_TAG, "type length not match!");
//			return null;
//		}
//		return new Pair<Type, Type>(type[0], type[1]);
//	}

}
