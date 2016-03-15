package com.skyline.db.jerrymouse.core.executor;

import android.content.ContentValues;
import android.database.Cursor;

import com.skyline.db.jerrymouse.core.exception.DataSourceException;

/**
 * Created by jairus on 15/12/22.
 */
public interface IExecutor {

	Cursor executeQuery(String sql, String[] args) throws DataSourceException;

	long executeInsert(String table, ContentValues values) throws DataSourceException;

	int executeUpdate(String table, ContentValues values, String whereClause, String[] whereArgs) throws DataSourceException;

	int executeDelete(String table, String whereClause, String[] whereArgs) throws DataSourceException;
}
