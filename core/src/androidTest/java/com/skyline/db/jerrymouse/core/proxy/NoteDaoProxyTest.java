package com.skyline.db.jerrymouse.core.proxy;

import android.test.AndroidTestCase;
import android.util.Log;

import com.skyline.db.jerrymouse.core.DaoProxy;
import com.skyline.db.jerrymouse.core.notepad.dao.NoteDao;
import com.skyline.db.jerrymouse.core.notepad.meta.Note;

import org.junit.Test;

/**
 * Created by apple on 16/3/10.
 */
public class NoteDaoProxyTest extends AndroidTestCase {

	private static final String LOG_TAG = NoteDaoProxyTest.class.getSimpleName();

	@Override
	protected void setUp() throws Exception {
		Log.d(LOG_TAG, "setUp");
		super.setUp();
		try {
			ProxyTestHelper.initDb(getContext());
		} catch (Throwable throwable) {
			Log.e(LOG_TAG, "fail to initDb", throwable);
		}
	}

	public Note addNote() throws Exception {
		long t1 = System.currentTimeMillis();
		Note note = new Note();
		note.title = "title " + System.currentTimeMillis();
		note.text = "text " + System.currentTimeMillis();
		note.deleted = false;
		note.createTime = System.currentTimeMillis();
		note.modifyTime = System.currentTimeMillis();

		long[] id = DaoProxy.getDao(NoteDao.class).insert(note);
		note.id = id[0];
		long t2 = System.currentTimeMillis();
		Log.d(LOG_TAG, "testAdd, cost: " + (t2 - t1));
		return note;
	}

	@Test
	public void testAdd() throws Throwable {

		long t1 = System.currentTimeMillis();
		Note note = new Note();
		note.title = "title " + System.currentTimeMillis();
		note.text = "text " + System.currentTimeMillis();
		note.deleted = false;
		note.createTime = System.currentTimeMillis();
		note.modifyTime = System.currentTimeMillis();

		DaoProxy.getDao(NoteDao.class).insert(note);
		long t2 = System.currentTimeMillis();
		Log.d(LOG_TAG, "testAdd, cost: " + (t2 - t1));
	}


//	@Test
//	public void testDelete1() throws Exception {
//		Note note = addNote();
//		Log.d(LOG_TAG, "testAdd, note: " + note);
//		int deleteItemNum = DaoProxy.getDao(NoteDao.class).delete(note);
//		Log.d(LOG_TAG, "testDelete1, deleteItemNum: " + deleteItemNum);
//	}

//	@Test
//	public void testDelete2() throws Exception {
//		Note note = addNote();
//		Log.d(LOG_TAG, "testAdd, note: " + note);
//		int deleItemNum = DaoProxy.getDao(NoteDao.class).delete(note.createTime);
//		Log.d(LOG_TAG, "testDelete2, deleItemNum: " + deleItemNum);
//	}

//	@Test
//	public void testUpdate1() throws Exception {
//		Note note = addNote();
//		Log.d(LOG_TAG, "testAdd, note: " + note);
//		note.title = "new title";
//		int updateItemNum = DaoProxy.getDao(NoteDao.class).update(note);
//		Log.d(LOG_TAG, "testUpdate1, updateItemNum: " + updateItemNum);
//	}

	@Test
	public void testUpdate2() throws Exception {
		Note note = addNote();
		Log.d(LOG_TAG, "testAdd, note: " + note);
		long updateItemNum = DaoProxy.getDao(NoteDao.class).update(note);
		Log.d(LOG_TAG, "testUpdate2, updateItemNum: " + updateItemNum);
	}

//		@Test
//	public void testSelect() throws Exception {
//		for(int i=0;i<10;i++) {
//			long t1 = System.currentTimeMillis();
//			Note note = DaoProxy.getDao(NoteDao.class).selectItem(false);
//				Log.d(LOG_TAG, "testSelect, note: " + note);
//		}
//	}

//	@Test
//	public void testSelect() throws Exception {
//		for(int i=0;i<10;i++) {
//			long t1 = System.currentTimeMillis();
//			List<Note> notes = DaoProxy.getDao(NoteDao.class).select(0);
//			long t2 = System.currentTimeMillis();
//			Log.d(LOG_TAG, "testSelect, notes: " + notes.size() + ", cost; " + (t2 - t1));
//			for (Note note : notes) {
//				Log.d(LOG_TAG, "testSelect, note: " + note);
//			}
//		}
//	}

//	@Test
//	public void testSelect2() throws Exception {
//		for(int i=0;i<10;i++) {
//			long t1 = System.currentTimeMillis();
//			List<Note> notes = DaoProxy.getDao(NoteDao.class).select(true);
//			long t2 = System.currentTimeMillis();
//			Log.d(LOG_TAG, "testSelect, notes: " + notes.size() + ", cost; " + (t2 - t1));
//			for (Note note : notes) {
//				Log.d(LOG_TAG, "testSelect, note: " + note);
//			}
//		}
//	}

//	@Test
//	public void testCount() throws Exception {
//		for (int i = 0; i < 10; i++) {
//			long t1 = System.currentTimeMillis();
//			double cnt = DaoProxy.getDao(NoteDao.class).count(false);
//			long t2 = System.currentTimeMillis();
//			Log.d(LOG_TAG, "testSelect, cnt: " + cnt + ", cost; " + (t2 - t1));
//		}
//	}

}
