package com.skyline.db.jerrymouse.core.notepad.dao;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.annotation.DeleteSql;
import com.skyline.db.jerrymouse.core.annotation.Param;
import com.skyline.db.jerrymouse.core.annotation.SelectSql;
import com.skyline.db.jerrymouse.core.annotation.Sql;
import com.skyline.db.jerrymouse.core.annotation.UpdateSql;
import com.skyline.db.jerrymouse.core.annotation.Value;
import com.skyline.db.jerrymouse.core.notepad.meta.Note;
import com.skyline.db.jerrymouse.core.notepad.util.BooleanMapper;
import com.skyline.db.jerrymouse.core.type.SqlType;

import java.util.List;

/**
 * Created by jairus on 16/3/10.
 */
public interface NoteDao extends Dao<Note> {

	@Sql(type = SqlType.DELETE, delete = @DeleteSql(whereClause = "createTime=?"))
	int delete(@Param long createTime);

	@Sql(type = SqlType.UPDATE, update = @UpdateSql(whereClause = "createTime=?"))
	int update(@Value(columnName = "title") String title, @Param long createTime);

	@Sql(type = SqlType.SELECT, select = @SelectSql(sql = "select * from " + Note.TABLE_NAME + " where createTime > ?"))
	List<Note> select(@Param long createTime);

	@Sql(type = SqlType.SELECT, select = @SelectSql(sql = "select * from " + Note.TABLE_NAME + " where deleted = ?"))
	List<Note> select(@Param(mapper = BooleanMapper.class) boolean deleted);

	@Sql(type = SqlType.SELECT, select = @SelectSql(sql = "select count(1) from " + Note.TABLE_NAME + " where deleted = ?"))
	double count(@Param(mapper = BooleanMapper.class) boolean deleted);
}
