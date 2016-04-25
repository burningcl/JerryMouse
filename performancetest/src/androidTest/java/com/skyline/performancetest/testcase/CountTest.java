package com.skyline.performancetest.testcase;

import android.test.AndroidTestCase;
import android.util.Log;

import com.skyline.db.jerrymouse.core.DaoProxy;
import com.skyline.performancetest.jerrymouse.JerryMouseDbInitHelper;
import com.skyline.performancetest.jerrymouse.JerryMouseNoteDao;
import com.skyline.performancetest.ormlite.OrmLiteDBHelper;
import com.skyline.performancetest.ormlite.OrmLiteNoteDao;

import org.junit.Test;

/**
 * Created by jairus on 16/4/23.
 */
public class CountTest extends AndroidTestCase {

	private static final String LOG_TAG = CountTest.class.getSimpleName();

	@Override
	protected void setUp() throws Exception {
		OrmLiteDBHelper.getHelper(getContext());
		JerryMouseDbInitHelper.initDb(getContext());
	}

	@Test
	public void test() throws Exception {
		int n = 500;
		OrmLiteNoteDao ormLiteNoteDao = new OrmLiteNoteDao();
		for (int i = 0; i < 5; i++) {
			System.gc();
			Thread.sleep(500);
			long t1 = System.currentTimeMillis();
			int size= DaoProxy.getDao(JerryMouseNoteDao.class).count(false);
			long t2 = System.currentTimeMillis();
			System.gc();
			Thread.sleep(500);
			long t3 = System.currentTimeMillis();
			long size2  = ormLiteNoteDao.count(0);
			long t4 = System.currentTimeMillis();
			long jc = (t2 - t1);
			long oc = (t4 - t3);
			Log.i(LOG_TAG, "CountTest, JerryMouse cost: " + jc + ", OrmLite cost: " + oc + ", speed up: " + ((double) oc / jc) + ", " + size+ "," +
					"" + size2);
		}
	}
}
