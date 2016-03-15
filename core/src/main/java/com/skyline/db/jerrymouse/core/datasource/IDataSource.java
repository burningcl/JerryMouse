package com.skyline.db.jerrymouse.core.datasource;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by jairus on 15-3-23.
 */
public interface IDataSource {

	Cursor rawQuery(String sql, String[] selectionArgs);

	void execSQL(String sql);

	long insert(String table, ContentValues values);

	int update(String table, ContentValues values, String whereClause, String[] whereArgs);

	int delete(String table, String whereClause, String[] whereArgs);

}
