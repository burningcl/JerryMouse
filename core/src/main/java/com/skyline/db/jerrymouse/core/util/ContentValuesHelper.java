package com.skyline.db.jerrymouse.core.util;

import android.content.ContentValues;

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
		} else {
			values.put(key, value.toString());
		}
	}
}
