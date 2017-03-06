package com.skyline.db.jerrymouse.core.mapper.ormapper;

import android.database.Cursor;
import android.util.Log;

import com.skyline.db.jerrymouse.core.LRUCache;
import com.skyline.db.jerrymouse.core.log.LogUtil;

import java.lang.reflect.Type;

/**
 * Created by apple on 16/3/15.
 */
public class PrimitiveDataMapper<T> extends AbsOrMapper<T> implements IOrMapper<T> {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = PrimitiveDataMapper.class.getSimpleName();

	public static LRUCache<Type, PrimitiveDataMapper> CACHE = new LRUCache<>(32);

	private Type type;

	private PrimitiveDataMapper(Type type) {
		this.type = type;
	}

	public static IOrMapper getInstance(Type type) {
		if (type == null) {
			LogUtil.w(LOG_TAG, "getInstance, type is null, pls check");
			return null;
		}
		PrimitiveDataMapper mapper = CACHE.get(type);
		if (mapper != null) {
			return mapper;
		}
		synchronized (CACHE) {
			mapper = CACHE.get(type);
			if (mapper != null) {
				return mapper;
			}
			mapper = new PrimitiveDataMapper(type);
			CACHE.put(type, mapper);
		}
		return mapper;
	}

	@Override
	public T map(Cursor c) throws IllegalAccessException, InstantiationException {
		if (c == null || !c.moveToNext()) {
			return null;
		}
		return (T) getFieldValue(c, 0, type);
	}
}
