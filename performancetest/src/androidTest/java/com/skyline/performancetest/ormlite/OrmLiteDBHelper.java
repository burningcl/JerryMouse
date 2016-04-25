package com.skyline.performancetest.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.skyline.performancetest.Note;

import java.sql.SQLException;

/**
 * Created by jairus on 16/4/23.
 */
public class OrmLiteDBHelper extends OrmLiteSqliteOpenHelper {

	private static final String TABLE_NAME = "ormlite_test";

	private OrmLiteDBHelper(Context context) {
		super(context, TABLE_NAME, null, 4);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
	                     ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Note.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
	                      ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Note.class, true);
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static OrmLiteDBHelper instance;

	/**
	 * 单例获取该Helper
	 *
	 * @param context
	 * @return
	 */
	public static synchronized OrmLiteDBHelper getHelper(Context context) {
		if (instance == null) {
			synchronized (OrmLiteDBHelper.class) {
				if (instance == null)
					instance = new OrmLiteDBHelper(context);
			}
		}

		return instance;
	}

}