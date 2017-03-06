package com.skyline.db.jerrymouse.core.util;


import com.skyline.db.jerrymouse.core.annotation.DbField;
import com.skyline.db.jerrymouse.core.annotation.DbTable;
import com.skyline.db.jerrymouse.core.exception.ClassParseException;
import com.skyline.db.jerrymouse.core.log.LogUtil;
import com.skyline.db.jerrymouse.core.mapper.MapperNull;
import com.skyline.db.jerrymouse.core.mapper.typemapper.ITypeMapper;
import com.skyline.db.jerrymouse.core.meta.InstanceParseResult;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairus on 16/1/22.
 */
public class InstanceParser {

	private static final String LOG_TAG = InstanceParser.class.getSimpleName();

	/**
	 * @param instance
	 * @return
	 * @throws ClassParseException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static InstanceParseResult parse(Object instance) throws ClassParseException, InstantiationException, IllegalAccessException {
		if (instance == null) {
			return null;
		}

		InstanceParseResult result = new InstanceParseResult();

		result.tableName = parseTableInfo(instance);
		result.fieldParseResults = parseFieldInfos(instance);

		return result;
	}

	/**
	 * @param instance
	 * @return
	 * @throws ClassParseException
	 */
	public static String parseTableInfo(Object instance) throws ClassParseException {
		if (instance == null) {
			return null;
		}

		Class<?> clazz = instance.getClass();
		DbTable dbTable = clazz.getAnnotation(DbTable.class);
		if (dbTable == null) {
			LogUtil.e(LOG_TAG, "parseTableInfo, fail, can't find DbTable on " + clazz.getName());
			throw new ClassParseException(ClassParseException.Reason.DB_TABLE_ANNOTATION_REQUIRED);
		}

		String tableName = dbTable.name();
		return tableName;
	}

	/**
	 * @param instance
	 * @return
	 * @throws ClassParseException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static List<InstanceParseResult.FieldParseResult> parseFieldInfos(Object instance) throws ClassParseException, IllegalAccessException, InstantiationException {
		if (instance == null) {
			return null;
		}

		Class<?> clazz = instance.getClass();
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length <= 0) {
			return null;
		}

		List<InstanceParseResult.FieldParseResult> list = new ArrayList<>();
		for (Field field : fields) {
			InstanceParseResult.FieldParseResult fieldParseResult = parseFieldInfo(instance, field);
			if (fieldParseResult != null)
				list.add(fieldParseResult);
		}
		return list;
	}

	/**
	 * @param instance
	 * @param field
	 * @return
	 * @throws ClassParseException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static InstanceParseResult.FieldParseResult parseFieldInfo(Object instance, Field field) throws ClassParseException, IllegalAccessException, InstantiationException {
		if (instance == null || field == null) {
			return null;
		}

		DbField dbField = field.getAnnotation(DbField.class);
		if (dbField == null) {
			return null;
		}
		field.setAccessible(true);

		InstanceParseResult.FieldParseResult result = new InstanceParseResult.FieldParseResult();
		parseFieldInfo(instance, field, dbField, result);
		return result;
	}

	public static void parseFieldInfo(Object instance, Field field, DbField dbField, InstanceParseResult.FieldParseResult result) throws
			IllegalAccessException, InstantiationException {

		// get field name
		result.fieldName = field.getName();

		// get column name
		result.columnName = dbField.name();
		if (StringUtils.isEmpty(result.columnName)) {
			result.columnName = result.fieldName;
		}

		// get field value
		Object fieldValue = null;
		fieldValue = field.get(instance);
		result.fieldValue = fieldValue;

		//get column value
		Object columnValue = fieldValue;
		if (columnValue != null) {
			Class<? extends ITypeMapper> mapperClass = dbField.mapper();
			if (mapperClass != MapperNull.class) {
				ITypeMapper mapper = TypeMapperHelper.getInstance(mapperClass);
				if (mapper != null) {
					columnValue = mapper.mapMetaType(fieldValue);
				}
			}
		}

		result.columnValue = columnValue;
		result.primaryKey = dbField.primaryKey().primaryKey();
		result.autoIncrement = dbField.primaryKey().autoIncrement();
	}

	public static Object getColumnValue(Object instance, Field field, DbField dbField) throws IllegalAccessException, InstantiationException {
		Object fieldValue = field.get(instance);

		//get column value
		Object columnValue = fieldValue;
		if (columnValue != null) {
			Class<? extends ITypeMapper> mapperClass = dbField.mapper();
			if (mapperClass != MapperNull.class) {
				ITypeMapper mapper = TypeMapperHelper.getInstance(mapperClass);
				if (mapper != null) {
					columnValue = mapper.mapMetaType(fieldValue);
				}
			}
		}
		return columnValue;
	}

}
