package com.skyline.db.jerrymouse.core.proxy;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.annotation.DbField;
import com.skyline.db.jerrymouse.core.datasource.DataSourceHolder;
import com.skyline.db.jerrymouse.core.exception.ClassParseException;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.exception.MethodParseException;
import com.skyline.db.jerrymouse.core.util.InstanceParser;
import com.skyline.db.jerrymouse.core.util.MethodInvokeHelper;
import com.skyline.db.jerrymouse.core.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * Created by jairus on 15/12/22.
 */
public class DeleteMethodProxy extends AbsMethodProxy {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = UpdateMethodProxy.class.getSimpleName();

	private String sql;

	private DbField[] dbFields;

	private Field[] fields;

	public DeleteMethodProxy(Class<? extends Dao> clazz, Method method, String sql) throws MethodParseException {
		super(clazz, method);
		this.sql = sql;
	}

	@Override
	public synchronized void parseClassAnnotations() throws ClassParseException {
		if (clzAnnoParsed)
			return;
		super.parseClassAnnotations();
		fields = metaClass.getDeclaredFields();
		int size = fields.length;
		dbFields = new DbField[size];
		for (int i = 0; i < size; i++) {
			Field field = fields[i];

			DbField dbField = field.getAnnotation(DbField.class);
			dbFields[i] = dbField;
			if (dbField == null) {
				continue;
			}
		}
		clzAnnoParsed = true;
	}

	@Override
	public synchronized void parseMethodAnnotations() throws NoSuchMethodException, MethodParseException, InstantiationException, IllegalAccessException {
		if (mtdAnnoParsed)
			return;
		super.parseMethodAnnotations();
		mtdAnnoParsed = true;
	}

	@Override
	public Object invoke(Object[] args) throws IllegalAccessException, ClassParseException, InstantiationException, DataSourceException, SQLException {

		long delItemNum = 0;

		if (args == null || args.length <= 0) {
			Log.w(LOG_TAG, "invoke, fail, args is empty!");
			return genResult(delItemNum);
		}

		ArgsType argsType = getArgsType(args);

		if (argsType == ArgsType.ILLEGAL) {
			delItemNum = 0;
		} else if (argsType == ArgsType.ARRAY) {
			delItemNum = delItems((Object[]) args[0]);
		} else if (argsType == ArgsType.SINGLE) {
			delItemNum = delItems(args);
		} else {
			delItemNum = delWithSql(args);
		}

		return genResult(delItemNum);
	}

	private Object genResult(long result) {
		if (returnType == null) {
			return null;
		} else if (returnType.equals(Long.class) || returnType.equals(Long.TYPE)) {
			return result;
		} else {
			return null;
		}
	}

	private long delItems(Object[] items) throws DataSourceException, IllegalAccessException, ClassParseException, InstantiationException,
			SQLException {

		long delItemNum = 0;

		if (StringUtils.isEmpty(sql)) {
			sql = getSql();
		}

		SQLiteDatabase db = DataSourceHolder.DATA_SOURCE.getWritableDatabase();
		SQLiteStatement statement = db.compileStatement(sql);
		try {
			for (int i = 0; i < items.length; i++) {
				Object item = items[i];
				statement.clearBindings();
				int cursor = 0;
				for (int j = 0; j < fields.length; j++) {
					DbField dbField = dbFields[j];
					if (dbField == null || !dbField.primaryKey().primaryKey()) {
						continue;
					}

					Object columnValue = InstanceParser.getColumnValue(item, fields[j], dbField);
					bindArg(statement, columnValue, ++cursor);
				}

				delItemNum += statement.executeUpdateDelete();
			}
		} finally {
			statement.close();
		}

		return delItemNum;
	}

	private long delWithSql(Object[] args) throws IllegalAccessException, InstantiationException, DataSourceException, SQLException {
		SQLiteDatabase db = DataSourceHolder.DATA_SOURCE.getWritableDatabase();
		SQLiteStatement statement = db.compileStatement(sql);
		try {
			statement.clearBindings();
			int cursor = 0;
			String[] invokeArgs = MethodInvokeHelper.getWhereArgs(args, method);
			for (Object arg : invokeArgs) {
				bindArg(statement, arg, ++cursor);
			}
			return statement.executeUpdateDelete();
		} finally {
			statement.close();
		}
	}


	private String getSql() {
		if (!StringUtils.isEmpty(sql)) {
			return sql;
		}
		StringBuilder sql = new StringBuilder(120);
		sql.append("DELETE FROM ");
		sql.append(tableName);
		sql.append(" WHERE ");


		boolean firstWhereArg = true;

		for (int i = 0; i < dbFields.length; i++) {
			DbField dbField = dbFields[i];
			if (dbField == null || !dbField.primaryKey().primaryKey()) {
				continue;
			}

			// get column name
			String columnName = dbField.name();
			if (StringUtils.isEmpty(columnName)) {
				columnName = fields[i].getName();
			}

			if (!firstWhereArg) {
				sql.append(" AND ");
			}
			sql.append(columnName)
					.append(" = ?");
			firstWhereArg = false;
		}

		this.sql = sql.toString();
		Log.d(LOG_TAG, "getSql, sql: " + this.sql);

		return this.sql;
	}

}
