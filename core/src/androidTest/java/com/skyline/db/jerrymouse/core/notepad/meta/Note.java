package com.skyline.db.jerrymouse.core.notepad.meta;

import com.skyline.db.jerrymouse.core.annotation.DbField;
import com.skyline.db.jerrymouse.core.annotation.DbTable;
import com.skyline.db.jerrymouse.core.annotation.PrimaryKey;
import com.skyline.db.jerrymouse.core.notepad.util.BooleanMapper;
import com.skyline.db.jerrymouse.core.type.SortType;

/**
 * Created by jairus on 16/1/22.
 */
@DbTable(name = Note.TABLE_NAME)
public class Note {

	public static final String TABLE_NAME = "TABLE_TEST_NOTE";

	@DbField(
			primaryKey =
			@PrimaryKey(
					primaryKey = true,
					autoIncrement = true
			)
	)
	public long id;

	@DbField(notNull = true)
	public String title;

	@DbField(name = "content")
	public String text;

	@DbField(mapper = BooleanMapper.class, defaultValue = "0")
	public boolean deleted;

	@DbField(index = SortType.DESC)
	public long createTime;

	@DbField
	public long modifyTime;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder()
				.append("{ ")
				.append("id: " + id + ", ")
				.append("title: " + title + ", ")
				.append("text: " + text + ", ")
				.append("deleted: " + deleted + ", ")
				.append("createTime: " + createTime + ", ")
				.append("modifyTime: " + modifyTime)
				.append(" }");
		return sb.toString();

	}
}
