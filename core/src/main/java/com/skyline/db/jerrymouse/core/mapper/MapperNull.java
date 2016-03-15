package com.skyline.db.jerrymouse.core.mapper;

import android.database.Cursor;
import android.util.Log;

/**
 * Created by jairus on 15/12/22.
 */
public final class MapperNull implements IOrMapper, ITypeMapper {

	private static final String LOG_TAG = MapperNull.class.getSimpleName();

	@Override
	public Object map(Cursor c) {
		Log.d(LOG_TAG, "map(), but i just do nothing!");
		return null;
	}

	@Override
	public Object mapMetaType(Object type) {
		Log.d(LOG_TAG, "mapMetaType(), but i just do nothing!");
		return null;
	}

	@Override
	public Object mapDbType(Object type) {
		Log.d(LOG_TAG, "mapDbType(), but i just do nothing!");
		return null;
	}
}
