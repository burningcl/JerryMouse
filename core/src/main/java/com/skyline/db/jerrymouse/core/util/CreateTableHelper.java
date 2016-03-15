package com.skyline.db.jerrymouse.core.util;

import android.util.Log;
import android.util.Pair;

import com.skyline.db.jerrymouse.core.annotation.DbField;
import com.skyline.db.jerrymouse.core.annotation.DbTable;
import com.skyline.db.jerrymouse.core.annotation.PrimaryKey;
import com.skyline.db.jerrymouse.core.mapper.ITypeMapper;
import com.skyline.db.jerrymouse.core.mapper.MapperNull;
import com.skyline.db.jerrymouse.core.meta.CreateTableSql;
import com.skyline.db.jerrymouse.core.type.DbColumnType;
import com.skyline.db.jerrymouse.core.type.SortType;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by jairus on 16/1/20.
 */
public class CreateTableHelper {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = CreateTableHelper.class.getSimpleName();

	private static final String BLANK_SPACE = " ";

	public static CreateTableSql genCreateTableSql(Class<?> clazz) {
		if (clazz == null) {
			Log.w(LOG_TAG, "createTable, fail, clazz is null!");
		}
		DbTable dbTable = clazz.getAnnotation(DbTable.class);
		if (dbTable == null) {
			Log.w(LOG_TAG, "createTable, fail, clazz: " + clazz + ", could not found DbTable annotation!");
		}
		String tableName = dbTable.name();
		if (tableName == null || tableName.length() <= 0) {
			Log.w(LOG_TAG, "createTable, fail, clazz: " + clazz + ", could not found tableName!");
		}
		Field[] fields = clazz.getDeclaredFields();
		if (fields == null || fields.length <= 0) {
			Log.w(LOG_TAG, "createTable, fail, clazz: " + clazz + ", fields is null!");
		}

		CreateTableSql sql = new CreateTableSql();

		StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
		sb.append(tableName);
		sb.append(" (");
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			DbField dbField = field.getAnnotation(DbField.class);
			if (dbField == null) {
				continue;
			}
			appendColumnDef(sb, field, dbField);
			if (i < fields.length - 1) {
				sb.append(",");
			}

			//genCreateIndexSql
			addCreateIndexSql(tableName, dbField, field, sql);
		}
		sb.append(");");

		sql.sql = sb.toString();
		return sql;
	}

	private static void addCreateIndexSql(String tableName, DbField dbField, Field field, CreateTableSql sql) {
		if (sql == null) {
			return;
		}
		String createIndexSql = genCreateIndexSql(tableName, dbField, field);
		if (createIndexSql == null) {
			return;
		}
		if (sql.createIndexSql == null) {
			sql.createIndexSql = new ArrayList<>();
		}
		sql.createIndexSql.add(createIndexSql);
	}

	private static String genCreateIndexSql(String tableName, DbField dbField, Field field) {
		if (StringUtils.isEmpty(tableName) || dbField == null || field == null) {
			return null;
		}
		if (dbField.index().equals(SortType.NULL)) {
			return null;
		}

		String fieldName = getColumnName(field, dbField);
		fieldName = fieldName.toUpperCase();

		StringBuilder sb = new StringBuilder("CREATE INDEX IF NOT EXISTS ");
		sb.append("INDEX_")
				.append(tableName)
				.append("_")
				.append(fieldName).append(" ON ")
				.append(tableName)
				.append(BLANK_SPACE)
				.append("(")
				.append(fieldName)
				.append(BLANK_SPACE)
				.append(dbField.index().toString())
				.append(");");
		return sb.toString();
	}

	private static void appendColumnDef(StringBuilder sb, Field field, DbField dbField) {
		if (sb == null || field == null || dbField == null) {
			return;
		}
		sb.append(BLANK_SPACE);
		sb.append(getColumnName(field, dbField));

		sb.append(BLANK_SPACE);
		sb.append(getColumnType(field, dbField));

		PrimaryKey primaryKey = dbField.primaryKey();
		if (primaryKey != null && primaryKey.primaryKey()) {
			sb.append(BLANK_SPACE);
			sb.append("PRIMARY KEY");
			if (primaryKey.autoIncrement()) {
				sb.append(BLANK_SPACE);
				sb.append("AUTOINCREMENT");
			}
		}

		if (dbField.notNull()) {
			sb.append(BLANK_SPACE);
			sb.append("NOT NULL");
		}

		if (dbField.unique()) {
			sb.append(BLANK_SPACE);
			sb.append("UNIQUE");
		}

		if (!StringUtils.isEmpty(dbField.defaultValue())) {
			sb.append(BLANK_SPACE);
			sb.append("DEFAULT");
			sb.append(BLANK_SPACE);
			sb.append(dbField.defaultValue());
		}
	}

	private static String getColumnName(Field field, DbField dbField) {
		if (field == null || dbField == null) {
			return null;
		}
		String columnName = dbField.name();
		// if name is not setted in DbField, use the field name
		if (StringUtils.isEmpty(columnName)) {
			columnName = field.getName();
		}
		return columnName;
	}

	private static DbColumnType getColumnType(Field field, DbField dbField) {
		if (field == null || dbField == null) {
			return null;
		}
		Type columnType = null;
		Class<? extends ITypeMapper> mapperClass = dbField.mapper();
		if (mapperClass != null && !mapperClass.equals(MapperNull.class)) {
			Pair<Type, Type> typePair = TypeMapperHelper.getMapperGenericType(mapperClass);
			columnType = typePair.second;
		} else {
			columnType = field.getType();
		}

		return DbColumnType.get(columnType);
	}
}
