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
public class InsertTest extends AndroidTestCase {

	private static final String LOG_TAG = InsertTest.class.getSimpleName();

	@Override
	protected void setUp() throws Exception {
		OrmLiteDBHelper.getHelper(getContext());
		JerryMouseDbInitHelper.initDb(getContext());
	}

	public Note[] getNotes() {
		int n = 500;
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
			System.gc();
			Thread.sleep(500);
			long t1 = System.currentTimeMillis();
			DaoProxy.getDao(JerryMouseNoteDao.class).insert(notes);
			long t2 = System.currentTimeMillis();
			notes = getNotes();
			System.gc();
			Thread.sleep(500);
			long t3 = System.currentTimeMillis();
			ormLiteNoteDao.addNotes(notes);
			long t4 = System.currentTimeMillis();
			long jc = (t2 - t1);
			long oc = (t4 - t3);
			Log.i(LOG_TAG, "InsertTest, JerryMouse cost: " + jc + ", OrmLite cost: " + oc + ", speed up: " + ((double) oc / jc));
		}
	}
}
