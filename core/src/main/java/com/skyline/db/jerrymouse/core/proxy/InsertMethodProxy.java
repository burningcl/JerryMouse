package com.skyline.db.jerrymouse.core.proxy;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.annotation.DbField;
import com.skyline.db.jerrymouse.core.annotation.DbTable;
import com.skyline.db.jerrymouse.core.exception.ClassParseException;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.exception.MethodParseException;
import com.skyline.db.jerrymouse.core.log.LogUtil;
import com.skyline.db.jerrymouse.core.util.InstanceParser;
import com.skyline.db.jerrymouse.core.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairus on 15/12/22.
 */
public class InsertMethodProxy extends AbsMethodProxy {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = InsertMethodProxy.class.getSimpleName();

	private String tableName;

	private Field[] fields;

	private boolean[] autoIncrements;

	private DbField[] dbFields;

	private String sql;

	public InsertMethodProxy(Class<? extends Dao> clazz, Method method) throws MethodParseException {
		super(clazz, method);
	}

	@Override
	public void parseClassAnnotations() throws ClassParseException {
		if (clzAnnoParsed) {
			return;
		}
		super.parseClassAnnotations();
		if (tableName == null) {
			tableName = metaClass.getAnnotation(DbTable.class).name();
		}
		if (fields == null) {
			fields = metaClass.getDeclaredFields();
		}
		if (dbFields == null) {
			dbFields = new DbField[fields.length];
			autoIncrements = new boolean[fields.length];
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				dbFields[i] = field.getAnnotation(DbField.class);
				if (dbFields[i] == null) {
					continue;
				}
				field.setAccessible(true);
				autoIncrements[i] = dbFields[i].primaryKey().autoIncrement();
			}
		}
		clzAnnoParsed = true;
	}

	@Override
	public synchronized void parseMethodAnnotations() throws InstantiationException, IllegalAccessException, NoSuchMethodException, MethodParseException {
		if (mtdAnnoParsed) {
			return;
		}
		super.parseMethodAnnotations();
		mtdAnnoParsed = true;
	}

	@Override
	public Object invoke(Object[] args) throws IllegalAccessException, ClassParseException, InstantiationException, DataSourceException, NoSuchMethodException, InvocationTargetException, SQLException {

		if (args == null || args.length <= 0) {
			LogUtil.w(LOG_TAG, "invoke, fail, args is empty!");
			return null;
		}

		ArgsType argsType = getArgsType(args);
		Object result = null;
		if (argsType == ArgsType.ILLEGAL) {
			result = null;
		} else if (argsType == ArgsType.ARRAY) {
			result = invokeInternal((Object[]) args[0]);
		} else if (argsType == ArgsType.SINGLE) {
			result = invokeInternal(args);
		} else {
			result = null;
		}
		return result;
	}

	private String getSql() {

		if (this.sql != null) {
			return this.sql;
		}

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT");
		//sql.append(" OR REPLACE ");
		sql.append(" INTO ");
		sql.append(tableName);
		sql.append('(');

		List<String> columnNames = new ArrayList<>();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			DbField dbField = dbFields[i];
			if (field == null || dbField == null || autoIncrements[i]) {
				continue;
			}
			// get column name
			String columnName = dbField.name();
			if (StringUtils.isEmpty(columnName)) {
				columnName = field.getName();
			}
			columnNames.add(columnName);
		}

		int size = (columnNames != null && columnNames.size() > 0)
				? columnNames.size() : 0;
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				String colName = columnNames.get(i);
				sql.append((i > 0) ? "," : "");
				sql.append(colName);
			}
			sql.append(')');
			sql.append(" VALUES (");
			for (int i = 0; i < size; i++) {
				sql.append((i > 0) ? ",?" : "?");
			}
		} else {
			sql.append(") VALUES (NULL");
		}
		sql.append(')');

		this.sql = sql.toString();

		LogUtil.d(LOG_TAG, "getSql, sql: " + sql);

		return this.sql;
	}

	public Object invokeInternal(Object[] args) throws IllegalAccessException, ClassParseException, InstantiationException, DataSourceException, NoSuchMethodException, InvocationTargetException, SQLException {

		long[] ids = new long[args.length];
		SQLiteDatabase db = getWritableDatabase();
		int cursor;
		//使用SQLiteStatement会比使用ContentValues快近40%
		SQLiteStatement statement = db.compileStatement(getSql());
		try {
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				cursor = 0;
				statement.clearBindings();
				for (int j = 0; j < fields.length; j++) {
					DbField dbField = dbFields[j];
					if (dbField == null || autoIncrements[j]) {
						continue;
					}
					Object columnValue = InstanceParser.getColumnValue(arg, fields[j], dbField);
					bindArg(statement, columnValue, ++cursor);
				}
				ids[i] = statement.executeInsert();
			}
		} finally {
			statement.close();
		}

		if (returnType == null) {
			return null;
		} else if (returnType.equals(Long.TYPE) || returnType.equals(Long.class)) {
			return ids[0];
		} else if (returnType.equals(long[].class)) {
			return ids;
		} else if (returnType.equals(Long[].class)) {
			Long[] idsRet = new Long[args.length];
			for (int i = 0; i < ids.length; i++) {
				idsRet[i] = ids[i];
			}
			return ids;
		} else {
			return null;
		}
	}

}
