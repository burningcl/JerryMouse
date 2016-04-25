package com.skyline.performancetest.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.skyline.performancetest.Note;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jairus on 16/4/23.
 */
public class OrmLiteNoteDao {

	private Dao<Note, Long> dao;

	private Dao<Note, Long> getDao() throws SQLException {
		if (dao != null) {
			return dao;
		}
		dao = OrmLiteDBHelper.getHelper(null).getDao(Note.class);
		return dao;
	}

	public void addNotes(Note... notes) throws SQLException {
		Dao<Note, Long> dao = getDao();
		for (Note note : notes) {
			dao.create(note);
		}
	}

	public int updateNotes(Note... notes) throws SQLException {
		int cnt = 0;
		Dao<Note, Long> dao = getDao();
		for (Note note : notes) {
			cnt += dao.update(note);
		}
		return cnt;
	}

	public int updateNotes(String title, long id) throws SQLException {
		Dao<Note, Long> dao = getDao();
		UpdateBuilder<Note, Long> ub = dao.updateBuilder();
		ub.where().lt("id", id);
		ub.updateColumnValue("title",title);
		return ub.update();
	}

	public List<Note> selectNotes(int deleted) throws SQLException {
		Dao<Note, Long> dao = getDao();
		QueryBuilder<Note, Long> qb = dao.queryBuilder();
		qb.where().eq("deleted", deleted);
		return qb.query();
	}

	public Note selectNote(int deleted) throws SQLException {
		Dao<Note, Long> dao = getDao();
		QueryBuilder<Note, Long> qb = dao.queryBuilder();
		qb.where().eq("deleted", deleted);
		return qb.queryForFirst();
	}

	public long count(int deleted) throws SQLException {
		Dao<Note, Long> dao = getDao();
		QueryBuilder<Note, Long> qb = dao.queryBuilder();
		qb.where().eq("deleted", deleted);
		return qb.countOf();
	}

	public int delNotes(Note... notes) throws SQLException {
		int cnt = 0;
		Dao<Note, Long> dao = getDao();
		for (Note note : notes) {
			cnt += dao.delete(note);
		}
		return cnt;
	}

	public long delNotes(long minId) throws SQLException {
		int cnt = 0;
		Dao<Note, Long> dao = getDao();
		DeleteBuilder<Note, Long> db= dao.deleteBuilder();
		db.where().ge("id", minId);
		return db.delete();
	}

}
