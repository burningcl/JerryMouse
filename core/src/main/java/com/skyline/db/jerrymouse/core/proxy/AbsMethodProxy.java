package com.skyline.db.jerrymouse.core.proxy;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.annotation.DbTable;
import com.skyline.db.jerrymouse.core.datasource.DataSourceHolder;
import com.skyline.db.jerrymouse.core.datasource.SQLiteDataSource;
import com.skyline.db.jerrymouse.core.exception.ClassParseException;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.exception.MethodParseException;
import com.skyline.db.jerrymouse.core.executor.IExecutor;
import com.skyline.db.jerrymouse.core.executor.SQLiteExecutor;
import com.skyline.db.jerrymouse.core.util.GenericTypeHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by jairus on 15/12/22.
 */
public abstract class AbsMethodProxy {

	protected Class<? extends Dao> clazz;

	protected Method method;

	protected Class<?> returnType;

	protected Class<?> metaClass;

	protected String tableName;

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

	public void parseClassAnnotations() throws ClassParseException {
		Type[] types = GenericTypeHelper.parseGenericType(clazz);
		metaClass = (Class<?>) types[0];
		DbTable dbTable = metaClass.getAnnotation(DbTable.class);
		if (dbTable == null) {
			throw new ClassParseException(ClassParseException.Reason.DB_TABLE_ANNOTATION_REQUIRED);
		}
		tableName = dbTable.name();
	}

	public void parseMethodAnnotations() throws InstantiationException, IllegalAccessException, NoSuchMethodException, MethodParseException {
		returnType = method.getReturnType();
	}

	public abstract Object invoke(Object[] args) throws InstantiationException, IllegalAccessException, DataSourceException, ClassParseException;

	/**
	 * @param args
	 * @return
	 */
	protected boolean isMeta(Object... args) {
		if (args == null || args.length <= 0 || metaClass == null) {
			return false;
		}
		for (Object arg : args) {
			if (!arg.getClass().equals(metaClass)) {
				return false;
			}
		}
		return true;
	}
}
