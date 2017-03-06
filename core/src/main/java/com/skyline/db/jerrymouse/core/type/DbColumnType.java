package com.skyline.db.jerrymouse.core.type;

import android.util.Log;

import com.skyline.db.jerrymouse.core.log.LogUtil;

import java.lang.reflect.Type;

/**
 * Created by jairus on 16/1/20.
 */
public enum DbColumnType {

	REAL,
	TEXT,
	INTEGER;

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = DbColumnType.class.getSimpleName();

	/**
	 * @param type
	 * @return
	 */
	public static DbColumnType get(Type type) {
		if (type == null) {
			Log.w(LOG_TAG, "get, type is null!");
			return null;
		} else if (type == Integer.TYPE || type == Long.TYPE || type == Character.TYPE || type == Short.TYPE || type == Byte.TYPE
				|| type == Integer.class || type == Long.class || type == Character.class || type == Short.class || type == Byte.class) {
			return INTEGER;
		} else if (type == Double.class || type == Float.class
				|| type == Double.TYPE || type == Float.TYPE) {
			return REAL;
		} else if (type == String.class) {
			return TEXT;
		}
		LogUtil.w(LOG_TAG, "get, type " + type + " is not supported!");
		return null;
	}

}
