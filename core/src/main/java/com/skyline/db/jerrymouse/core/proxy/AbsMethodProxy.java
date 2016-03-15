package com.skyline.db.jerrymouse.core.proxy;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.datasource.DataSourceHolder;
import com.skyline.db.jerrymouse.core.datasource.SQLiteDataSource;
import com.skyline.db.jerrymouse.core.exception.ClassParseException;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.exception.MethodParseException;
import com.skyline.db.jerrymouse.core.executor.IExecutor;
import com.skyline.db.jerrymouse.core.executor.SQLiteExecutor;

import java.lang.reflect.Method;

/**
 * Created by jairus on 15/12/22.
 */
public abstract class AbsMethodProxy {

	protected Class<? extends Dao> clazz;

	protected Method method;

	protected Class<?> returnType;

	public AbsMethodProxy(Class<? extends Dao> clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
	}

	protected IExecutor getExecutor() throws DataSourceException {
		if (DataSourceHolder.DATA_SOURCE == null) {
			throw new DataSourceException(DataSourceException.Reason.DATA_SOURCE_NOT_INITED);
		} else if (DataSourceHolder.DATA_SOURCE instanceof SQLiteDataSource) {
			return SQLiteExecutor.geInstance();
		}
		throw new DataSourceException(DataSourceException.Reason.DATA_SOURCE_NOT_INITED);
	}

	public abstract void parseClassAnnotations() throws ClassParseException;

	public  void parseMethodAnnotations() throws InstantiationException, IllegalAccessException, NoSuchMethodException, MethodParseException {
		returnType = method.getReturnType();
	}

	public abstract Object invoke(Object[] args) throws InstantiationException, IllegalAccessException, DataSourceException, ClassParseException;
}
