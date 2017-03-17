package com.skyline.db.jerrymouse.core.mapper.ormapper;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.util.LruCache;

import com.skyline.db.jerrymouse.core.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairus on 16/1/20.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ByteCodeListMapper<T> implements IOrMapper<List<T>> {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = ByteCodeListMapper.class.getSimpleName();

	public static LruCache<Class, ByteCodeListMapper> CACHE = new LruCache<>(32);

	private Class<? extends T> clazz;

	private ByteCodeListMapper(Class<? extends T> clazz) {
		this.clazz = clazz;
	}

	public static IOrMapper getInstance(Class clazz) {
		if (clazz == null) {
			LogUtil.w(LOG_TAG, "getInstance, clazz is null, pls check");
			return null;
		}
		ByteCodeListMapper mapper = CACHE.get(clazz);
		if (mapper != null) {
			return mapper;
		}
		synchronized (CACHE) {
			mapper = CACHE.get(clazz);
			if (mapper != null) {
				return mapper;
			}
			mapper = new ByteCodeListMapper(clazz);
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
		IOrMapper mapper = ByteCodeMapper.getInstance(clazz);
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
