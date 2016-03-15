package com.skyline.db.jerrymouse.core.proxy;

import android.content.ContentValues;
import android.util.Log;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.exception.ClassParseException;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.exception.MethodParseException;
import com.skyline.db.jerrymouse.core.executor.IExecutor;
import com.skyline.db.jerrymouse.core.meta.InstanceParseResult;
import com.skyline.db.jerrymouse.core.util.ContentValuesHelper;
import com.skyline.db.jerrymouse.core.util.InstanceParser;

import java.lang.reflect.Method;

/**
 * Created by jairus on 15/12/22.
 */
public class InsertMethodProxy extends AbsMethodProxy {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = InsertMethodProxy.class.getSimpleName();

	public InsertMethodProxy(Class<? extends Dao> clazz, Method method) throws MethodParseException {
		super(clazz, method);
	}

	@Override
	public void parseClassAnnotations() {
	}

	@Override
	public synchronized void parseMethodAnnotations() throws InstantiationException, IllegalAccessException, NoSuchMethodException, MethodParseException {
		super.parseMethodAnnotations();
	}

	@Override
	public Object invoke(Object[] args) throws IllegalAccessException, ClassParseException, InstantiationException, DataSourceException {

		if (args == null || args.length <= 0) {
			Log.w(LOG_TAG, "invoke, fail, args is empty!");
			return null;
		}

		IExecutor executor = getExecutor();
		long[] ids = new long[args.length];
		for (int i = 0; i < args.length; i++) {
			Object arg = args[i];
			StringBuilder sqlSb = new StringBuilder();
			StringBuilder paramsSb = new StringBuilder();
			StringBuilder valuesSb = new StringBuilder();

			InstanceParseResult result = InstanceParser.parse(arg);
			String table = result.tableName;

			sqlSb.append("INSERT INTO ")
					.append(table);

			ContentValues values = new ContentValues();
			for (int j = 0; j < result.fieldParseResults.size(); j++) {
				InstanceParseResult.FieldParseResult r = result.fieldParseResults.get(j);
				if (r.autoIncrement) {
					continue;
				}
				ContentValuesHelper.put(r.columnName, r.columnValue, values);
				if (j == 0) {
					paramsSb.append("(");
					valuesSb.append("(");
				}
				paramsSb.append(r.columnName);
				valuesSb.append(r.columnValue);
				if (j < result.fieldParseResults.size() - 1) {
					paramsSb.append(", ");
					valuesSb.append(", ");
				} else {
					paramsSb.append(")");
					valuesSb.append(")");
				}
			}

			sqlSb.append(paramsSb);
			sqlSb.append(" VALUES ");
			sqlSb.append(valuesSb);
			sqlSb.append(";");

			ids[i] = executor.executeInsert(table, values);

			Log.i(LOG_TAG, "invoke, sql: " + sqlSb);
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
