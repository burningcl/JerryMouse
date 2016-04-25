package com.skyline.db.jerrymouse.core.executor;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.skyline.db.jerrymouse.core.exception.DataSourceException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

/**
 * Created by jairus on 15/12/22.
 */
public interface IExecutor {

	Cursor executeQuery(String sql, String[] args) throws DataSourceException;

	SQLiteDatabase getWritableDatabase() throws DataSourceException;

	long executeInsert(String table, ContentValues values) throws DataSourceException;

	long executeInsert(SQLiteDatabase db,SQLiteStatement statement, Object[] bindArgs) throws NoSuchMethodException, IllegalAccessException,
			InstantiationException, InvocationTargetException, DataSourceException, SQLException;

	int executeUpdate(String table, ContentValues values, String whereClause, String[] whereArgs) throws DataSourceException;

	int executeDelete(String table, String whereClause, String[] whereArgs) throws DataSourceException;

	void beginTransaction() throws DataSourceException;

	void setTransactionSuccessful() throws DataSourceException;

	void endTransaction() throws DataSourceException;
}
