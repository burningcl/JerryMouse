package com.skyline.db.jerrymouse.core.datasource;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jairus on 15-12-14.
 */
public interface DataSourceInitCallBack {

	/**
	 */
	void beforeCreateTable();

	/**
	 * @param database
	 * @param dataSource
	 */
	void afterCreateTable(SQLiteDatabase database, SQLiteDataSource dataSource);

	/**
	 * @param database
	 * @param oldVersion
	 * @param newVersion
	 * @param dataSource
	 */
	void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion, SQLiteDataSource dataSource);

}
