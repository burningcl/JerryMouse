package com.skyline.db.jerrymouse.core.util;


import com.skyline.db.jerrymouse.core.log.LogUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by jairus on 16/3/15.
 */
public class GenericTypeHelper {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = GenericTypeHelper.class.getSimpleName();

	/**
	 * @param clazz
	 * @return
	 */
	public static Type[] parseGenericType(Class<?> clazz) {
		if (clazz == null) {
			LogUtil.w(LOG_TAG, "clazz is null!");
			return null;
		}
		Type[] interfaceTypes = clazz.getGenericInterfaces();
		if (interfaceTypes == null || interfaceTypes.length <= 0) {
			Type superClass = clazz.getGenericSuperclass();
			if (superClass != null) {
				try {
					return parseGenericType((Class<?>) superClass);
				} catch (Exception ignore) {
					return null;
				}
			} else {
				return null;
			}

		}
		ParameterizedType pt = null;
		for (Type interfaceType : interfaceTypes) {
			if (interfaceType == null) {
				continue;
			}
			if (!(interfaceType instanceof ParameterizedType)) {
				continue;
			}
			pt = (ParameterizedType) interfaceType;
			break;
		}
		if (pt == null) {
			LogUtil.w(LOG_TAG, "ParameterizedType not found!");
			return null;
		}
		return pt.getActualTypeArguments();
	}
}
