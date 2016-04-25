package com.skyline.performancetest.jerrymouse;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.skyline.db.jerrymouse.core.datasource.DataSourceInitCallBack;
import com.skyline.db.jerrymouse.core.datasource.SQLiteDataSource;
import com.skyline.performancetest.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairus on 16/4/23.
 */
public class JerryMouseDbInitHelper {

	private static final String LOG_TAG = JerryMouseDbInitHelper.class.getSimpleName();

	private static boolean inited = false;

	public static void initDb(Context context) throws Exception {
		if (inited) {
			return;
		}
		String dbName = "jerrymouse_test";
		SQLiteDatabase.CursorFactory factory = null;
		int version = 2;
		DatabaseErrorHandler errorHandler = null;
		List<Class<?>> metaCalzzes = new ArrayList<>();
		metaCalzzes.add(Note.class);
		DataSourceInitCallBack initCallBack = new DataSourceInitCallBack() {
			@Override
			public void beforeCreateTable() {
				Log.i(LOG_TAG, "beforeCreateTable");
			}

			@Override
			public void afterCreateTable(SQLiteDatabase database, SQLiteDataSource dataSource) {
				Log.i(LOG_TAG, "afterCreateTable");
			}

			@Override
			public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion, SQLiteDataSource dataSource) {
				Log.i(LOG_TAG, "onUpgrade, oldVersion: " + oldVersion + ", newVersion: " + newVersion);
			}
		};
		SQLiteOpenHelper sQLiteOpenHelper = (SQLiteOpenHelper) SQLiteDataSource.init(context,
				dbName,
				factory,
				version,
				errorHandler,
				metaCalzzes,
				initCallBack,
				Log.INFO);
		Log.d(LOG_TAG, "sQLiteOpenHelper: " + sQLiteOpenHelper);
		Log.d(LOG_TAG, "sQLiteOpenHelper.getWritableDatabase: " + sQLiteOpenHelper.getWritableDatabase());
		inited = true;
	}
}
