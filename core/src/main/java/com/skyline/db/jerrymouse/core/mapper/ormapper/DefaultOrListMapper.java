package com.skyline.db.jerrymouse.core.mapper.ormapper;

import android.database.Cursor;
import android.util.Log;
import android.util.LruCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairus on 16/1/20.
 */
public class DefaultOrListMapper<T> implements IOrMapper<List<T>> {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = DefaultOrListMapper.class.getSimpleName();

	public static LruCache<Class, DefaultOrListMapper> CACHE = new LruCache<>(32);

	private Class<? extends T> clazz;

	private DefaultOrListMapper(Class<? extends T> clazz) {
		this.clazz = clazz;
	}

	public static IOrMapper getInstance(Class clazz) {
		if (clazz == null) {
			Log.w(LOG_TAG, "getInstance, clazz is null, pls check");
			return null;
		}
		DefaultOrListMapper mapper = CACHE.get(clazz);
		if (mapper != null) {
			return mapper;
		}
		synchronized (CACHE) {
			mapper = CACHE.get(clazz);
			if (mapper != null) {
				return mapper;
			}
			mapper = new DefaultOrListMapper(clazz);
			CACHE.put(clazz, mapper);
		}
		return mapper;
	}

	@Override
	public List<T> map(Cursor c) throws IllegalAccessException, InstantiationException {
		if (c == null) {
			return null;
		}
		List<T> list = new ArrayList<>(c.getCount());
		IOrMapper mapper = DefaultOrMapper.getInstance(clazz);
		while (true) {
			T item = (T) mapper.map(c);
			if (item == null) {
				break;
			}
			list.add(item);
		}
		return list;
	}


}
