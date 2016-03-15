package com.skyline.db.jerrymouse.core.util;

import android.util.Log;
import android.util.Pair;

import com.skyline.db.jerrymouse.core.LRUCache;
import com.skyline.db.jerrymouse.core.mapper.ITypeMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by jairus on 15/12/22.
 */
public class TypeMapperHelper {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = TypeMapperHelper.class.getSimpleName();

	/**
	 * CACHE
	 */
	private static final LRUCache<Class<? extends ITypeMapper>, ITypeMapper> CACHE = new LRUCache<>(32);

	/**
	 * GENERIC_TYPE_CACHE
	 */
	private static final LRUCache<Class<? extends ITypeMapper>, Pair<Type, Type>> GENERIC_TYPE_CACHE = new LRUCache<>(32);

	/**
	 * @param clazz
	 * @return
	 */
	private static ITypeMapper getFromCache(Class<? extends ITypeMapper> clazz) {
		if (clazz == null) {
			return null;
		}
		return CACHE.get(clazz);
	}

	/**
	 * @param clazz
	 * @param mapper
	 */
	private static void putIntoCache(Class<? extends ITypeMapper> clazz, ITypeMapper mapper) {
		if (clazz == null) {
			return;
		}
		CACHE.put(clazz, mapper);
	}

	/**
	 * @param clazz
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static ITypeMapper getInstance(Class<? extends ITypeMapper> clazz) throws IllegalAccessException, InstantiationException {
		ITypeMapper mapper = getFromCache(clazz);
		if (mapper != null) {
			return mapper;
		}
		mapper = clazz.newInstance();
		putIntoCache(clazz, mapper);
		return mapper;
	}

	/**
	 * @param clazz
	 * @return
	 */
	public static Pair<Type, Type> getMapperGenericType(Class<? extends ITypeMapper> clazz) {
		if (clazz == null) {
			Log.w(LOG_TAG, "clazz is null!");
			return null;
		}

		Pair<Type, Type> pair = GENERIC_TYPE_CACHE.get(clazz);
		if (pair != null) {
			return pair;
		}

		synchronized (clazz) {
			pair = GENERIC_TYPE_CACHE.get(clazz);
			if (pair != null) {
				return pair;
			}

			pair = parseMapperGenericType(clazz);
			if (pair != null) {
				GENERIC_TYPE_CACHE.put(clazz, pair);
			}
		}

		return pair;

	}

	/**
	 * @param clazz
	 * @return
	 */
	private static Pair<Type, Type> parseMapperGenericType(Class<? extends ITypeMapper> clazz) {
		if (clazz == null) {
			Log.w(LOG_TAG, "clazz is null!");
			return null;
		}
		Type[] interfaceTypes = clazz.getGenericInterfaces();
		if (interfaceTypes == null || interfaceTypes.length <= 0) {
			Type superClass = clazz.getGenericSuperclass();
			if (superClass != null && superClass instanceof Class<?>) {
				try {
					return parseMapperGenericType((Class<? extends ITypeMapper>) superClass);
				} catch (Exception ignore) {
					return null;
				}
			} else {
				return null;
			}

		}
		String canonicalName = ITypeMapper.class.getCanonicalName();
		if (canonicalName == null) {
			return null;
		} else {
			canonicalName = canonicalName + "<";
		}
		ParameterizedType pt = null;
		for (Type interfaceType : interfaceTypes) {
			if (interfaceType == null) {
				continue;
			}
			String typeStr = interfaceType.toString();
			if (!(typeStr.startsWith(canonicalName) && typeStr.endsWith(">"))) {
				continue;
			}
			if (!(interfaceType instanceof ParameterizedType)) {
				continue;
			}
			pt = (ParameterizedType) interfaceType;
			break;
		}
		if (pt == null) {
			Log.w(LOG_TAG, "ParameterizedType not found!");
			return null;
		}
		Type[] type = pt.getActualTypeArguments();
		if (type == null || type.length != 2) {
			Log.w(LOG_TAG, "type length not match!");
			return null;
		}
		return new Pair<Type, Type>(type[0], type[1]);
	}
}
