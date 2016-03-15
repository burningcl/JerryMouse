package com.skyline.db.jerrymouse.core.util;

import android.content.ContentValues;
import android.util.Log;

/**
 * Created by jairus on 16/1/25.
 */
public class ContentValuesHelper {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = ContentValuesHelper.class.getSimpleName();

	public static void put(String key, Object value, ContentValues values) {
		if (key == null || values == null) {
			return;
		}
		if (value == null) {
			values.put(key, StringUtils.VALUE_NULL_STR);
		}
		Class<?> clazz = value.getClass();
		if (clazz == Integer.TYPE || clazz == Integer.class) {
			values.put(key, (Integer) value);
		} else if (clazz == Long.TYPE || clazz == Long.class) {
			values.put(key, (Long) value);
		} else if (clazz == Double.TYPE || clazz == Double.class) {
			values.put(key, (Double) value);
		} else if (clazz == Float.TYPE || clazz == Float.class) {
			values.put(key, (Float) value);
		} else if (clazz == Short.TYPE || clazz == Short.class) {
			values.put(key, (Short) value);
		} else if (clazz == Byte.TYPE || clazz == Byte.class) {
			values.put(key, (Byte) value);
		} else if (clazz == String.class) {
			values.put(key, (String) value);
		} else {
			Log.w(LOG_TAG, "get, clazz " + clazz + " is not supported!");
		}

	}
}
