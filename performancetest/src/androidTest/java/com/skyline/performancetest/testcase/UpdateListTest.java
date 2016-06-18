package com.skyline.performancetest.testcase;

import android.test.AndroidTestCase;
import android.util.Log;

import com.skyline.db.jerrymouse.core.DaoProxy;
import com.skyline.performancetest.Note;
import com.skyline.performancetest.jerrymouse.JerryMouseDbInitHelper;
import com.skyline.performancetest.jerrymouse.JerryMouseNoteDao;
import com.skyline.performancetest.ormlite.OrmLiteDBHelper;
import com.skyline.performancetest.ormlite.OrmLiteNoteDao;

/**
 * Created by jairus on 16/4/23.
 */
public class UpdateListTest extends AndroidTestCase {

	private static final String LOG_TAG = UpdateListTest.class.getSimpleName();

	@Override
	protected void setUp() throws Exception {
		OrmLiteDBHelper.getHelper(getContext());
		JerryMouseDbInitHelper.initDb(getContext());
	}

	public void test() throws Exception {

		OrmLiteNoteDao ormLiteNoteDao = new OrmLiteNoteDao();

		int n = 200;
		Note[] notes = new Note[n];
		for (int i = 1; i <= n; i++) {
			Note note = new Note();
			note.id = i;
			note.title = "new title " + System.currentTimeMillis();
			note.text = "new text " + System.currentTimeMillis();
			note.deleted = 1;
			note.createTime = System.currentTimeMillis();
			note.modifyTime = System.currentTimeMillis();
			notes[i-1] = note;
		}

		for (int i = 0; i < 5; i++) {
			System.gc();
			Thread.sleep(500);
			long t1 = System.currentTimeMillis();
			long count = DaoProxy.getDao(JerryMouseNoteDao.class).update(notes);
			long t2 = System.currentTimeMillis();
			System.gc();
			Thread.sleep(500);
			long t3 = System.currentTimeMillis();
			long count2 = ormLiteNoteDao.updateNotes(notes);
			long t4 = System.currentTimeMillis();
			long jc = (t2 - t1);
			long oc = (t4 - t3);
			Log.i(LOG_TAG, "UpdateListTest, JerryMouse cost: " + jc + ", OrmLite cost: " + oc + ", speed up: " + ((double) oc / jc) + ", " + count + "," +
					"" + count2);
		}
	}
}
