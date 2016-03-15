package com.skyline.db.jerrymouse.core.util;

import android.util.Log;

import com.skyline.db.jerrymouse.core.meta.InstanceParseResult;
import com.skyline.db.jerrymouse.core.notepad.meta.Note;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by jairus on 16/1/25.
 */
public class InstanceParserTest extends TestCase {

	private static final String LOG_TAG = InstanceParserTest.class.getSimpleName();

	@Test
	public void test() throws Throwable {
		Note note = new Note();
		note.id = 100;
		note.title = "this is title";
		note.text = "this is text! this is content!";
		note.deleted = false;
		note.createTime = System.currentTimeMillis();
		note.modifyTime = System.currentTimeMillis() + 1;

		InstanceParseResult result = InstanceParser.parse(note);
		Log.d(LOG_TAG, "tableName: " + result.tableName);
		for (InstanceParseResult.FieldParseResult r : result.fieldParseResults) {
			Log.d(LOG_TAG, "primaryKey: " + r.primaryKey
					+ ",\tautoIncrement: " + r.autoIncrement
					+ ",\tcolumnName: " + r.columnName
					+ ",\tcolumnValue: " + r.columnValue
					+ ",\tfieldName: " + r.fieldName
					+ ",\tfieldValue: " + r.fieldValue);
		}
	}
}
