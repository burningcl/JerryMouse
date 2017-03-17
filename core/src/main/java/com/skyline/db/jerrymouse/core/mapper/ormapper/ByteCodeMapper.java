package com.skyline.db.jerrymouse.core.mapper.ormapper;

import android.database.Cursor;
import android.util.LruCache;

import com.skyline.db.jerrymouse.core.log.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Created by chenliang on 2017/3/13.
 */

public class ByteCodeMapper extends AbsOrMapper<Object> implements IOrMapper<Object> {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = DefaultOrMapper.class.getSimpleName();

	public static LruCache<Class, ByteCodeMapper> CACHE = new LruCache<>(32);

	public AbsOrMapper<Object> mapper;

	private ByteCodeMapper(Class<?> clazz) throws InstantiationException, IllegalAccessException {
		String className = clazz.getSimpleName();
		className = "generated.mappper." + className + "Mapper";
		try {
			Class<?> mapperClass = Class.forName(className);
			LogUtil.d(LOG_TAG, "ByteCodeMapper, className: " + className);
			mapper = (AbsOrMapper<Object>) mapperClass.newInstance();
		} catch (ClassNotFoundException e) {
			LogUtil.d(LOG_TAG, "ByteCodeMapper, className: " + className, e);
			mapper = (AbsOrMapper<Object>) DefaultOrMapper.getInstance(clazz);
		}

	}

	public static IOrMapper getInstance(Class clazz) throws IllegalAccessException, InstantiationException {
		if (clazz == null) {
			LogUtil.w(LOG_TAG, "getInstance, clazz is null, pls check");
			return null;
		}
		ByteCodeMapper mapper = CACHE.get(clazz);
		if (mapper != null) {
			return mapper;
		}
		synchronized (CACHE) {
			mapper = CACHE.get(clazz);
			if (mapper != null) {
				return mapper;
			}
			mapper = new ByteCodeMapper(clazz);
			CACHE.put(clazz, mapper);
		}
		return mapper;
	}

	@Override
	public Object map(Cursor c) throws IllegalAccessException, InstantiationException {
		return mapper.map(c);
	}


}
