package com.skyline.db.jerrymouse.core.proxy;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.annotation.DbField;
import com.skyline.db.jerrymouse.core.exception.ClassParseException;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.exception.MethodParseException;
import com.skyline.db.jerrymouse.core.log.LogUtil;
import com.skyline.db.jerrymouse.core.util.InstanceParser;
import com.skyline.db.jerrymouse.core.util.MethodInvokeHelper;
import com.skyline.db.jerrymouse.core.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jairus on 15/12/22.
 */
public class UpdateMethodProxy extends AbsMethodProxy {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = UpdateMethodProxy.class.getSimpleName();

	private String sql;

	private DbField[] dbFields;

	private Field[] fields;

	public UpdateMethodProxy(Class<? extends Dao> clazz, Method method, String sql) throws MethodParseException {
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

		long updateItemNum = 0;

		if (args == null || args.length <= 0) {
			LogUtil.w(LOG_TAG, "invoke, fail, args is empty!");
			return genResult(updateItemNum);
		}

		ArgsType argsType = getArgsType(args);

		if (argsType == ArgsType.ILLEGAL) {
			updateItemNum = 0;
		} else if (argsType == ArgsType.ARRAY) {
			updateItemNum = updateItems((Object[]) args[0]);
		} else if (argsType == ArgsType.SINGLE) {
			updateItemNum = updateItems(args);
		} else {
			updateItemNum = updateWithSql(args);
		}

		return genResult(updateItemNum);
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

	private long updateItems(Object[] items) throws DataSourceException, IllegalAccessException, ClassParseException, InstantiationException,
			SQLException {

		long updateItemNum = 0;

		if (StringUtils.isEmpty(sql)) {
			sql = getSql();
		}

		SQLiteDatabase db = getWritableDatabase();
		SQLiteStatement statement = db.compileStatement(sql);
		try {
			List<Object> whereArgList = new ArrayList<>();
			for (int i = 0; i < items.length; i++) {
				Object item = items[i];
				statement.clearBindings();
				whereArgList.clear();
				int cursor = 0;
				for (int j = 0; j < fields.length; j++) {
					DbField dbField = dbFields[j];
					if (dbField == null) {
						continue;
					}

					Object columnValue = InstanceParser.getColumnValue(item, fields[j], dbField);
					if (dbField.primaryKey().primaryKey()) {
						whereArgList.add(columnValue);
					} else {
						bindArg(statement, columnValue, ++cursor);
					}
				}

				for (Object whereArg : whereArgList) {
					bindArg(statement, whereArg, ++cursor);
				}

				updateItemNum += statement.executeUpdateDelete();
			}
		} finally {
			statement.close();
		}

		return updateItemNum;
	}

	private long updateWithSql(Object[] args) throws IllegalAccessException, InstantiationException, DataSourceException, SQLException {
		SQLiteDatabase db = getWritableDatabase();
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
		sql.append("UPDATE ");
		sql.append(tableName);
		sql.append(" SET ");

		StringBuilder whereClause = new StringBuilder(" WHERE ");

		boolean firstVal = true;
		boolean firstWhereArg = true;

		for (int i = 0; i < dbFields.length; i++) {
			DbField dbField = dbFields[i];
			if (dbField == null) {
				continue;
			}

			// get column name
			String columnName = dbField.name();
			if (StringUtils.isEmpty(columnName)) {
				columnName = fields[i].getName();
			}

			if (dbField.primaryKey().primaryKey()) {
				if (!firstWhereArg) {
					whereClause.append(" AND ");
				}
				whereClause.append(columnName)
						.append(" = ?");
				firstWhereArg = false;
			} else {
				if (!firstVal) {
					sql.append(" , ");
				}
				sql.append(columnName)
						.append(" = ?");
				firstVal = false;
			}
		}

		sql.append(whereClause);
		this.sql = sql.toString();

		LogUtil.d(LOG_TAG, "getSql, sql: " + this.sql);

		return this.sql;
	}

}
