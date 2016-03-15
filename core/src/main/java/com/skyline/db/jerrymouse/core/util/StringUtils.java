package com.skyline.db.jerrymouse.core.util;

/**
 * Created by apple on 16/1/20.
 */
public class StringUtils {

	public static final String EMPTY_STR = "";

	public static final String VALUE_NULL_STR = "NULL";

	public static boolean isEmpty(String str) {
		return str == null || str.length() <= 0;
	}
}
