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
public class DelTest extends AndroidTestCase {

	private static final String LOG_TAG = DelTest.class.getSimpleName();

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

	@Test
	public void test() throws Exception {
		OrmLiteNoteDao ormLiteNoteDao = new OrmLiteNoteDao();
		for (int i = 0; i < 5; i++) {
			System.gc();
			Thread.sleep(500);
			long t1 = System.currentTimeMillis();
			Note note= DaoProxy.getDao(JerryMouseNoteDao.class).selectItem(false);
			long t2 = System.currentTimeMillis();
			System.gc();
			Thread.sleep(500);
			long t3 = System.currentTimeMillis();
			Note note2 = ormLiteNoteDao.selectNote(0);
			long t4 = System.currentTimeMillis();
			long jc = (t2 - t1);
			long oc = (t4 - t3);
			Log.i(LOG_TAG, "SelectItemTest, JerryMouse cost: " + jc + ", OrmLite cost: " + oc + ", speed up: " + ((double) oc / jc) + ", " + note+ "," +
					"" + note2);
		}
	}
}
