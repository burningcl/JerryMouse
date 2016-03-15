package com.skyline.db.jerrymouse.core.meta;

import java.util.List;

/**
 * Created by jairus on 16/1/22.
 */
public class InstanceParseResult {

	/**
	 *
	 */
	public String tableName;

	/**
	 *
	 */
	public List<FieldParseResult> fieldParseResults;

	/**
	 *
	 */
	public static class FieldParseResult {

		/**
		 *
		 */
		public String columnName;

		/**
		 *
		 */
		public Object columnValue;

		/**
		 *
		 */
		public String fieldName;

		/**
		 *
		 */
		public Object fieldValue;

		/**
		 *
		 */
		public boolean primaryKey;

		/**
		 *
		 */
		public boolean autoIncrement;

	}
}
