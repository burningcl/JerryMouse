package com.skyline.db.jerrymouse.core.mapper;

import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Type;

/**
 * Created by jairus on 16/1/20.
 */
public abstract class AbsOrMapper<T> implements IOrMapper<T> {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = AbsOrMapper.class.getSimpleName();

	protected Object getFieldValue(Cursor cursor, int columnIndex, Type type) {
		if (cursor == null || type == null) {
			Log.w(LOG_TAG, "getFieldValue, fail, cursor, columnName or type is null");
			return null;
		}
		if (type.equals(Long.TYPE) || type.equals(Long.class)) {
			return cursor.getLong(columnIndex);
		} else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
			return cursor.getInt(columnIndex);
		} else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
			return cursor.getDouble(columnIndex);
		} else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
			return cursor.getFloat(columnIndex);
		} else if (type.equals(Short.TYPE) || type.equals(Short.class)) {
			return cursor.getShort(columnIndex);
		} else if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
			return (byte) cursor.getShort(columnIndex);
		} else if (type.equals(String.class)) {
			return cursor.getString(columnIndex);
		} else {
			Log.w(LOG_TAG, "getFieldValue, fail, type " + type + " is not a primitive data type!");
			return null;
		}
	}

}
