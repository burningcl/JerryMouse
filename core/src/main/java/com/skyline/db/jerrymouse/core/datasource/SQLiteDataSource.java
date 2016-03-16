package com.skyline.db.jerrymouse.core.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.meta.CreateTableSql;
import com.skyline.db.jerrymouse.core.util.CreateTableHelper;

import java.util.List;

/**
 * Created by jairus on 15-4-3.
 */
public class SQLiteDataSource extends SQLiteOpenHelper implements IDataSource {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = SQLiteDataSource.class.getSimpleName();

	private static List<Class<?>> META_CALZZES;

	private static DataSourceInitCallBack INIT_CALL_BACK;

	private static SQLiteDataSource INSTANCE;

	private SQLiteDataSource(Context context,
	                         String name,
	                         SQLiteDatabase.CursorFactory factory,
	                         int version,
	                         DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	public synchronized static IDataSource init(Context context,
	                                            String name,
	                                            SQLiteDatabase.CursorFactory factory,
	                                            int version,
	                                            DatabaseErrorHandler errorHandler,
	                                            List<Class<?>> metaCalzzes,
	                                            DataSourceInitCallBack initCallBack) throws DataSourceException {
		Log.i(LOG_TAG, "init, context: " + context + ", name: " + name + ", version: " + version);
		if (INSTANCE != null && DataSourceHolder.DATA_SOURCE != null) {
			throw new DataSourceException(DataSourceException.Reason.DATA_SOURCE_ALREADY_INITED);
		}
		META_CALZZES = metaCalzzes;
		INIT_CALL_BACK = initCallBack;
		INSTANCE = new SQLiteDataSource(context, name, factory, version, errorHandler);
		DataSourceHolder.DATA_SOURCE = INSTANCE;
		return INSTANCE;
	}

	@Override
	public Cursor rawQuery(String sql, String[] selectionArgs) {
		return getReadableDatabase().rawQuery(sql, selectionArgs);
	}

	@Override
	public void execSQL(String sql) {
		getWritableDatabase().execSQL(sql);
	}

	@Override
	public long insert(String table, ContentValues values) {
		return getWritableDatabase().insert(table, null, values);
	}

	@Override
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		return getWritableDatabase().update(table, values, whereClause, whereArgs);
	}

	@Override
	public int delete(String table, String whereClause, String[] whereArgs) {
		return getWritableDatabase().delete(table, whereClause, whereArgs);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(LOG_TAG, "onCreate");
		if (INIT_CALL_BACK != null) {
			INIT_CALL_BACK.beforeCreateTable();
		}
		createTables(db);
		if (INIT_CALL_BACK != null) {
			INIT_CALL_BACK.afterCreateTable(db, INSTANCE);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(LOG_TAG, "onUpgrade, oldVersion: " + oldVersion + ", newVersion: " + newVersion);
		createTables(db);
		if (INIT_CALL_BACK != null) {
			INIT_CALL_BACK.onUpgrade(db, oldVersion, newVersion, INSTANCE);
		}
	}

	private void createTables(SQLiteDatabase db) {
		for (Class<?> clazz : META_CALZZES) {
			CreateTableSql sql = CreateTableHelper.genCreateTableSql(clazz);
			Log.i(LOG_TAG, "create table, sql: " + sql.sql);
			db.execSQL(sql.sql);
			if (sql.createIndexSql != null) {
				for (String createIndexSql : sql.createIndexSql) {
					Log.i(LOG_TAG, "create index, sql: " + createIndexSql);
					db.execSQL(createIndexSql);
				}
			}
		}
	}

}