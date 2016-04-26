package com.skyline.db.jerrymouse.core.proxy;

import android.database.Cursor;
import android.util.Log;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.annotation.Mapper;
import com.skyline.db.jerrymouse.core.exception.ClassParseException;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.exception.MethodParseException;
import com.skyline.db.jerrymouse.core.mapper.MapperNull;
import com.skyline.db.jerrymouse.core.mapper.ormapper.DefaultOrListMapper;
import com.skyline.db.jerrymouse.core.mapper.ormapper.DefaultOrMapper;
import com.skyline.db.jerrymouse.core.mapper.ormapper.IOrMapper;
import com.skyline.db.jerrymouse.core.mapper.ormapper.PrimitiveDataMapper;
import com.skyline.db.jerrymouse.core.util.MethodInvokeHelper;
import com.skyline.db.jerrymouse.core.util.PrimitiveDataTypeUtil;
import com.skyline.db.jerrymouse.core.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jairus on 15/12/22.
 */
public class SelectMethodProxy extends AbsMethodProxy {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = SelectMethodProxy.class.getSimpleName();

	private boolean raw;

	private IOrMapper returnMapper;

	private String sql;

	private Mapper mapperAnnotation;

	private boolean methodAnnotationsParsed = false;

	public SelectMethodProxy(Class<? extends Dao> clazz, Method method, String sql, Mapper mapperAnnotation) throws MethodParseException {
		super(clazz, method);
		if (StringUtils.isEmpty(sql)) {
			throw new MethodParseException(MethodParseException.Reason.SELECT_SQL_REQUIRED);
		}
		this.sql = sql;
		this.mapperAnnotation = mapperAnnotation;
	}

	@Override
	public void parseClassAnnotations() throws ClassParseException {
		super.parseClassAnnotations();
	}

	@Override
	public synchronized void parseMethodAnnotations() throws InstantiationException, IllegalAccessException, NoSuchMethodException, MethodParseException {
		if (methodAnnotationsParsed) {
			return;
		}
		super.parseMethodAnnotations();
		parseSqlAnnotation();
		methodAnnotationsParsed = true;
	}

	private void parseSqlAnnotation() throws IllegalAccessException, InstantiationException, NoSuchMethodException, MethodParseException {
		raw = mapperAnnotation.raw();
		Class<? extends IOrMapper> mapperClass = null;
		if (!raw) {
			mapperClass = mapperAnnotation.mapper();
			if (!mapperClass.equals(MapperNull.class)) {
				returnMapper = mapperClass.newInstance();
			} else {
				if (returnType == null) {
					returnMapper = null;
				} else if (returnType.equals(List.class)) {
					returnMapper = DefaultOrListMapper.getInstance(metaClass);
				} else if (PrimitiveDataTypeUtil.isPrimitiveDataType(returnType)) {
					returnMapper = PrimitiveDataMapper.getInstance(returnType);
				} else {
					returnMapper = DefaultOrMapper.getInstance(returnType);
				}
			}
		}
		Log.d(LOG_TAG, "parseSqlAnnotation, raw: " + raw + ", mapperClass: " + mapperClass + ", returnType: " + returnType);
	}

	@Override
	public Object invoke(Object[] args) throws InstantiationException, IllegalAccessException, DataSourceException {

		long t1 = System.currentTimeMillis();
		String[] invokeArgs = MethodInvokeHelper.getWhereArgs(args, method);

		Log.i(LOG_TAG, "invoke, sql: " + sql + ", invokeArgs: " + Arrays.toString(invokeArgs));

		Cursor cursor = getReadableDatabase().rawQuery(sql, invokeArgs);
		long t2 = System.currentTimeMillis();

		if (raw) {
			return cursor;
		}

		try {
			if (returnMapper != null) {
				Log.i(LOG_TAG, "invoke, returnMapper: " + returnMapper + ", count: " + cursor.getCount());
				return returnMapper.map(cursor);
			} else
				return null;
		} finally {
			long t3 = System.currentTimeMillis();
			Log.d(LOG_TAG, "invoke, query cost: " + (t2 - t1) + ", map cost: " + (t3 - t2));
			if (cursor != null && !cursor.isClosed()) {
				try {
					cursor.close();
				} catch (Exception ignore) {
				}
			}
		}
	}

}
