package com.skyline.db.jerrymouse.core.util;

import android.content.ContentValues;

import com.skyline.db.jerrymouse.core.annotation.Param;
import com.skyline.db.jerrymouse.core.annotation.Value;
import com.skyline.db.jerrymouse.core.mapper.ITypeMapper;
import com.skyline.db.jerrymouse.core.mapper.MapperNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairus on 16/1/22.
 */
public class MethodInvokeHelper {

	public static final String[] STRING_TEMPLATE = new String[0];

	/**
	 * @param args
	 * @param method
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static String[] getWhereArgs(Object[] args, Method method) throws InstantiationException, IllegalAccessException {
		List<String> whereArgs = new ArrayList<>();
		Annotation[][] annotations = method.getParameterAnnotations();
		for (int i = 0; i < args.length; i++) {
			Param paramAnnotation = null;
			Object arg = args[i];
			for (int j = 0; j < annotations[i].length; j++) {
				Annotation ca = annotations[i][j];
				if (ca instanceof Param) {
					paramAnnotation = (Param) ca;
				}
			}
			if (paramAnnotation == null) {
				continue;
			}
			if (arg != null && paramAnnotation.mapper() != null && paramAnnotation.mapper() != MapperNull.class) {
				ITypeMapper mapper = TypeMapperHelper.getInstance(paramAnnotation.mapper());
				arg = mapper.mapMetaType(arg);
			}
			if (arg != null)
				whereArgs.add(arg.toString());
			else
				whereArgs.add(null);
		}
		return whereArgs.toArray(STRING_TEMPLATE);
	}

	/**
	 *
	 * @param args
	 * @param method
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static ContentValues getValueArgs(Object[] args, Method method) throws InstantiationException, IllegalAccessException {
		ContentValues values = new ContentValues();
		Annotation[][] annotations = method.getParameterAnnotations();
		for (int i = 0; i < args.length; i++) {
			Value valueAnnotation = null;
			Object arg = args[i];
			for (int j = 0; j < annotations[i].length; j++) {
				Annotation ca = annotations[i][j];
				if (ca instanceof Value) {
					valueAnnotation = (Value) ca;
				}
			}
			if (valueAnnotation == null) {
				continue;
			}
			if (arg != null && valueAnnotation.mapper() != null && valueAnnotation.mapper() != MapperNull.class) {
				ITypeMapper mapper = TypeMapperHelper.getInstance(valueAnnotation.mapper());
				arg = mapper.mapMetaType(arg);
			}
			String columnName = valueAnnotation.columnName();

			ContentValuesHelper.put(columnName, arg, values);
		}
		return values;
	}


}
