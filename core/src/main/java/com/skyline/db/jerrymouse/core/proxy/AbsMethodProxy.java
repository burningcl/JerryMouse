package com.skyline.db.jerrymouse.core.proxy;

import android.database.sqlite.SQLiteStatement;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.annotation.DbTable;
import com.skyline.db.jerrymouse.core.datasource.DataSourceHolder;
import com.skyline.db.jerrymouse.core.datasource.SQLiteDataSource;
import com.skyline.db.jerrymouse.core.exception.ClassParseException;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.exception.MethodParseException;
import com.skyline.db.jerrymouse.core.executor.IExecutor;
import com.skyline.db.jerrymouse.core.executor.SQLiteExecutor;
import com.skyline.db.jerrymouse.core.type.DbColumnType;
import com.skyline.db.jerrymouse.core.util.GenericTypeHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.SQLException;

/**
 * Created by jairus on 15/12/22.
 */
public abstract class AbsMethodProxy {

	protected Class<? extends Dao> clazz;

	protected Method method;

	protected Class<?> returnType;

	protected Class<?> metaClass;

	protected String tableName;

	protected boolean clzAnnoParsed = false;

	protected boolean mtdAnnoParsed = false;

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

	public abstract Object invoke(Object[] args) throws InstantiationException, IllegalAccessException, DataSourceException, ClassParseException, NoSuchMethodException, InvocationTargetException, SQLException;


	public enum ArgsType {
		/**
		 * single instance
		 */
		SINGLE,
		/**
		 * instance array
		 */
		ARRAY,
		/**
		 * other
		 */
		OTHER,
		/**
		 *
		 */
		ILLEGAL

	}

	ArgsType argsType;

	/**
	 * @param args
	 * @return
	 */
	protected ArgsType getArgsType(Object... args) {
		if (argsType != null) {
			return argsType;
		}
		argsType = getArgsTypeInternal(args);
		return argsType;
	}

	/**
	 * @param args
	 * @return
	 */
	protected ArgsType getArgsTypeInternal(Object... args) {
		if (args == null || args.length <= 0 || metaClass == null) {
			return ArgsType.ILLEGAL;
		}
		if (args.length == 1) {
			Object arg0 = args[0];
			if (arg0 == null) {
				return ArgsType.ILLEGAL;
			} else if (arg0 instanceof Object[]) {
				Object[] invokeArgs = (Object[]) args[0];
				for (Object arg : invokeArgs) {
					if (!arg.getClass().equals(metaClass)) {
						return ArgsType.ILLEGAL;
					}
				}
				return ArgsType.ARRAY;
			} else {
				if (arg0.getClass().equals(metaClass)) {
					return ArgsType.SINGLE;
				} else {
					return ArgsType.OTHER;
				}
			}
		}
		return ArgsType.OTHER;
	}

	protected void bindArg(SQLiteStatement stmt, Object arg, int index) throws SQLException {
		if (arg == null) {
			stmt.bindNull(index);
		} else {
			Class<?> clazz = arg.getClass();
			DbColumnType dbColumnType = DbColumnType.get(clazz);
			if (dbColumnType == null) {
				stmt.bindNull(index);
			} else if (dbColumnType == DbColumnType.INTEGER) {
				stmt.bindLong(index, ((Number) arg).longValue());
			} else if (dbColumnType == DbColumnType.REAL) {
				stmt.bindDouble(index, ((Number) arg).doubleValue());
			} else {
				stmt.bindString(index, arg.toString());
			}
		}
	}
}
