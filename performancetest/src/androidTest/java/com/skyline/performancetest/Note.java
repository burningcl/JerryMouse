package com.skyline.performancetest;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.skyline.db.jerrymouse.core.annotation.DbField;
import com.skyline.db.jerrymouse.core.annotation.DbTable;
import com.skyline.db.jerrymouse.core.annotation.PrimaryKey;

/**
 * Created by jairus on 16/1/22.
 */
@DatabaseTable(tableName = Note.TABLE_NAME)
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
	@DatabaseField(generatedId = true)
	public long id;

	@DbField
	@DatabaseField
	public String title;

	@DbField(name = "content")
	@DatabaseField(columnName = "content")
	public String text;

	@DbField
	@DatabaseField
	public int deleted;

	@DbField
	@DatabaseField
	public long createTime;

	@DbField
	@DatabaseField
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
