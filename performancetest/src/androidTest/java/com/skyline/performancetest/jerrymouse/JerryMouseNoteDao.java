package com.skyline.performancetest.jerrymouse;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.annotation.Param;
import com.skyline.db.jerrymouse.core.annotation.Sql;
import com.skyline.db.jerrymouse.core.mapper.typemapper.BooleanTypeMapper;
import com.skyline.db.jerrymouse.core.type.SqlType;
import com.skyline.performancetest.Note;

import java.util.List;

/**
 * Created by jairus on 16/3/10.
 */
public interface JerryMouseNoteDao extends Dao<Note> {

	@Sql(type = SqlType.DELETE, value = "delete from " + Note.TABLE_NAME + " where id>=?")
	long delete(@Param long id);

	@Sql(type = SqlType.UPDATE, value = "update " + Note.TABLE_NAME + " set title=? where id<?")
	long update(@Param String title, @Param long id);

	@Sql(type = SqlType.SELECT, value = "select * from " + Note.TABLE_NAME + " where createTime > ?")
	List<Note> select(@Param long createTime);

	@Sql(type = SqlType.SELECT, value = "select * from " + Note.TABLE_NAME + " where deleted = ? limit 0,1")
	Note selectItem(@Param(mapper = BooleanTypeMapper.class) boolean deleted);

	@Sql(type = SqlType.SELECT, value = "select * from " + Note.TABLE_NAME + " where deleted = ?")
	List<Note> select(@Param(mapper = BooleanTypeMapper.class) boolean deleted);

	@Sql(type = SqlType.SELECT, value = "select count(1) from " + Note.TABLE_NAME + " where deleted = ?")
	int count(@Param(mapper = BooleanTypeMapper.class) boolean deleted);
}
