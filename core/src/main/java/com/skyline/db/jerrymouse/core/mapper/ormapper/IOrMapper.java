package com.skyline.db.jerrymouse.core.mapper.ormapper;

import android.database.Cursor;

/**
 * Created by jairus on 15-12-15.
 */
public interface IOrMapper<T> {

	/**
	 * @param c DB Cursor
	 * @return
	 */
	T map(Cursor c) throws IllegalAccessException, InstantiationException;

}
