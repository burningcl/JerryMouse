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
public class DelWithSqlTest extends AndroidTestCase {

	private static final String LOG_TAG = DelWithSqlTest.class.getSimpleName();

	@Override
	protected void setUp() throws Exception {
		OrmLiteDBHelper.getHelper(getContext());
		JerryMouseDbInitHelper.initDb(getContext());
	}

	public static int n = 500;

	public Note[] getNotes() {
		Note[] notes = new Note[n];
		for (int i = 0; i < n; i++) {
			Note note = new Note();
			note.title = "title " + System.currentTimeMillis();
			note.text = "text " + System.currentTimeMillis();
			note.deleted = 0;
			note.createTime = System.currentTimeMillis();
			note.modifyTime = System.currentTimeMillis();
			notes[i] = note;
		}
		return notes;
	}

	public void test() throws Exception {

		OrmLiteNoteDao ormLiteNoteDao = new OrmLiteNoteDao();
		for (int i = 0; i < 5; i++) {
			Note[] notes = getNotes();
			long[] ids = DaoProxy.getDao(JerryMouseNoteDao.class).insert(notes);
			for (int j = 0; j < 500; j++) {
				notes[j].id = ids[j];
			}
			System.gc();
			Thread.sleep(100);
			long t1 = System.currentTimeMillis();
			long size = DaoProxy.getDao(JerryMouseNoteDao.class).delete(ids[0]);
			long t2 = System.currentTimeMillis();
			notes = getNotes();
			ormLiteNoteDao.addNotes(notes);
			System.gc();
			Thread.sleep(100);
			long t3 = System.currentTimeMillis();
			long size2 = ormLiteNoteDao.delNotes(notes[0].id);
			long t4 = System.currentTimeMillis();
			long jc = (t2 - t1);
			long oc = (t4 - t3);
			Log.i(LOG_TAG, "DelWithSqlTest, JerryMouse cost: " + jc + ", OrmLite cost: " + oc + ", speed up: " + ((double) oc / jc) + ", " + size + ", " +
					"" + size2);
		}
	}
}
