package com.skyline.performancetest.testcase;

import android.test.AndroidTestCase;
import android.util.Log;

import com.skyline.db.jerrymouse.core.DaoProxy;
import com.skyline.performancetest.Note;
import com.skyline.performancetest.jerrymouse.JerryMouseDbInitHelper;
import com.skyline.performancetest.jerrymouse.JerryMouseNoteDao;
import com.skyline.performancetest.ormlite.OrmLiteDBHelper;
import com.skyline.performancetest.ormlite.OrmLiteNoteDao;

import org.junit.Test;

/**
 * Created by jairus on 16/4/23.
 */
public class SelectItemTest extends AndroidTestCase {

	private static final String LOG_TAG = SelectItemTest.class.getSimpleName();

	@Override
	protected void setUp() throws Exception {
		OrmLiteDBHelper.getHelper(getContext());
		JerryMouseDbInitHelper.initDb(getContext());
	}

	public void test() throws Exception {
		OrmLiteNoteDao ormLiteNoteDao = new OrmLiteNoteDao();
		for (int i = 0; i < 5; i++) {
			System.gc();
			Thread.sleep(500);
			long t1 = System.nanoTime();
			Note note= DaoProxy.getDao(JerryMouseNoteDao.class).selectItem(false);
			long t2 = System.nanoTime();
			System.gc();
			Thread.sleep(500);
			long t3 = System.nanoTime();
			Note note2 = ormLiteNoteDao.selectNote(0);
			long t4 = System.nanoTime();
			long jc = (t2 - t1);
			long oc = (t4 - t3);
			Log.i(LOG_TAG, "SelectItemTest, JerryMouse cost: " + jc + ", OrmLite cost: " + oc + ", speed up: " + ((double) oc / jc) + ", " + note+ "," +
					"" + note2);
		}
	}
}
