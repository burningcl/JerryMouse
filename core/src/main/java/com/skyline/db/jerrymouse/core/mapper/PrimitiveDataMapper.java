package com.skyline.db.jerrymouse.core.mapper;

import android.database.Cursor;
import android.util.Log;

import com.skyline.db.jerrymouse.core.LRUCache;

/**
 * Created by apple on 16/3/15.
 */
public class PrimitiveDataMapper<T> extends AbsOrMapper<T> implements IOrMapper<T> {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = PrimitiveDataMapper.class.getSimpleName();

	public static LRUCache<Class, PrimitiveDataMapper> CACHE = new LRUCache<>(32);

	private Class<? extends T> clazz;

	private PrimitiveDataMapper(Class<? extends T> clazz) {
		this.clazz = clazz;
	}

	public static IOrMapper getInstance(Class clazz) {
		if (clazz == null) {
			Log.w(LOG_TAG, "getInstance, clazz is null, pls check");
			return null;
		}
		PrimitiveDataMapper mapper = CACHE.get(clazz);
		if (mapper != null) {
			return mapper;
		}
		synchronized (CACHE) {
			mapper = CACHE.get(clazz);
			if (mapper != null) {
				return mapper;
			}
			mapper = new PrimitiveDataMapper(clazz);
			CACHE.put(clazz, mapper);
		}
		return mapper;
	}

	@Override
	public T map(Cursor c) throws IllegalAccessException, InstantiationException {
		if (c == null || !c.moveToNext()) {
			Log.w(LOG_TAG, "map, fail, c is null or empty");
			return (T)Integer.valueOf(0);
		}
		return (T) super.getFieldValue(c, 0, clazz);
	}
}
