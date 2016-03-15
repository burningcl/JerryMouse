package com.skyline.db.jerrymouse.core.util;

import android.util.Log;

import com.skyline.db.jerrymouse.core.meta.CreateTableSql;
import com.skyline.db.jerrymouse.core.notepad.meta.Note;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by jairus on 15/12/21.
 */
public class CreateSqlHelperTest extends TestCase {

	private final String LOG_TAG = "CreateSqlHelperTest";

	@Test
	public void test() throws Throwable {

		CreateTableSql sql = CreateTableHelper.genCreateTableSql(Note.class);
		Log.i(LOG_TAG, "CreateTableSql: " + sql.sql);
		if(sql.createIndexSql!=null){
			for(String createIndexSql:sql.createIndexSql){
				Log.i(LOG_TAG, "CreateIndexSql: " + createIndexSql);
			}
		}
	}

}