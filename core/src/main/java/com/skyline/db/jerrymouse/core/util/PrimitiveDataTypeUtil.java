package com.skyline.db.jerrymouse.core.util;

/**
 * Created by jairus on 16/3/15.
 */
public class PrimitiveDataTypeUtil {

	public static boolean isPrimitiveDataType(Class<?> clazz) {
		if (clazz == null) {
			return false;
		}
		if (clazz.equals(Long.TYPE) || clazz.equals(Long.class)) {
			return true;
		} else if (clazz.equals(Integer.TYPE) || clazz.equals(Integer.class)) {
			return true;
		} else if (clazz.equals(Double.TYPE) || clazz.equals(Double.class)) {
			return true;
		} else if (clazz.equals(Float.TYPE) || clazz.equals(Float.class)) {
			return true;
		} else if (clazz.equals(Short.TYPE) || clazz.equals(Short.class)) {
			return true;
		} else if (clazz.equals(Byte.TYPE) || clazz.equals(Byte.class)) {
			return true;
		}
		return false;
	}
}
