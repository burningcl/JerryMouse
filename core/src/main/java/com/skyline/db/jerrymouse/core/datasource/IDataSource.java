package com.skyline.db.jerrymouse.core.datasource;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Created by jairus on 15-3-23.
 */
public interface IDataSource {

	Cursor rawQuery(String sql, String[] selectionArgs);

	void execSQL(String sql);

	long insert(String table, ContentValues values);

	SQLiteDatabase getWritableDatabase();

	SQLiteDatabase getReadableDatabase() ;

	long insert(SQLiteDatabase db, SQLiteStatement statement, Object[] bindArgs) throws NoSuchMethodException, IllegalAccessException, InstantiationException,
			InvocationTargetException, SQLException;

	int update(String table, ContentValues values, String whereClause, String[] whereArgs);

	int delete(String table, String whereClause, String[] whereArgs);

	void beginTransaction();

	void setTransactionSuccessful();

	void endTransaction();

}
