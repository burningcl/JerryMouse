package com.skyline.db.jerrymouse.core.executor;

import android.content.ContentValues;
import android.database.Cursor;

import com.skyline.db.jerrymouse.core.datasource.DataSourceHolder;
import com.skyline.db.jerrymouse.core.datasource.SQLiteDataSource;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;

/**
 * Created by jairus on 15/12/22.
 */
public class SQLiteExecutor implements IExecutor {

	private static SQLiteExecutor INSTANCE;

	public static SQLiteExecutor geInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		}
		INSTANCE = new SQLiteExecutor();
		return INSTANCE;
	}

	@Override
	public Cursor executeQuery(String sql, String[] args) throws DataSourceException {
		return getDataSource().rawQuery(sql, args);
	}

	@Override
	public long executeInsert(String table, ContentValues values) throws DataSourceException {
		return getDataSource().insert(table, values);
	}

	@Override
	public int executeUpdate(String table, ContentValues values, String whereClause, String[] whereArgs) throws DataSourceException {
		return getDataSource().update(table, values, whereClause, whereArgs);
	}

	@Override
	public int executeDelete(String table, String whereClause, String[] whereArgs) throws DataSourceException {
		return getDataSource().delete(table, whereClause, whereArgs);
	}

	public SQLiteDataSource getDataSource() throws DataSourceException {
		if (DataSourceHolder.DATA_SOURCE == null) {
			throw new DataSourceException(DataSourceException.Reason.DATA_SOURCE_NOT_INITED);
		} else if (!(DataSourceHolder.DATA_SOURCE instanceof SQLiteDataSource)) {
			throw new DataSourceException(DataSourceException.Reason.DATA_SOURCE_DOES_NOT_MATCH);
		}
		SQLiteDataSource dataSource = (SQLiteDataSource) DataSourceHolder.DATA_SOURCE;
		return dataSource;
	}
}
