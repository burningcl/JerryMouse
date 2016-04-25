package com.skyline.db.jerrymouse.core.proxy.legacy;

import com.skyline.db.jerrymouse.core.Dao;
import com.skyline.db.jerrymouse.core.exception.ClassParseException;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.proxy.AbsMethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * Created by jairus on 15/12/22.
 */
public class DeleteMethodProxy extends AbsMethodProxy {
	public DeleteMethodProxy(Class<? extends Dao> clazz, Method method) {
		super(clazz, method);
	}

	@Override
	public Object invoke(Object[] args) throws InstantiationException, IllegalAccessException, DataSourceException, ClassParseException, NoSuchMethodException, InvocationTargetException, SQLException {
		return null;
	}

//	/**
//	 * LOG_TAG
//	 */
//	private static final String LOG_TAG = DeleteMethodProxy.class.getSimpleName();
//
//	private DeleteSql deleteSql;
//
//	public DeleteMethodProxy(Class<? extends Dao> clazz, Method method, DeleteSql deleteSql) throws MethodParseException {
//		super(clazz, method);
//		if (deleteSql == null) {
//			throw new MethodParseException(MethodParseException.Reason.DELETE_SQL_REQUIRED);
//		}
//		this.deleteSql = deleteSql;
//	}
//
//	@Override
//	public void parseClassAnnotations() throws ClassParseException {
//		super.parseClassAnnotations();
//	}
//
//	@Override
//	public synchronized void parseMethodAnnotations() throws NoSuchMethodException, MethodParseException, InstantiationException, IllegalAccessException {
//		super.parseMethodAnnotations();
//	}
//
//	@Override
//	public Object invoke(Object[] args) throws InstantiationException, IllegalAccessException, DataSourceException, ClassParseException {
//
//		int delItemNum = 0;
//
//		if (args == null || args.length <= 0) {
//			Log.w(LOG_TAG, "invoke, fail, args is empty!");
//			return genResult(delItemNum);
//		}
//
//		ArgsType argsType = getArgsType(args);
//
//		if (argsType == ArgsType.ILLEGAL) {
//			delItemNum = 0;
//		} else if (argsType == ArgsType.ARRAY) {
//			delItemNum = deleteItems((Object[]) args[0]);
//		} else if (argsType == ArgsType.SINGLE) {
//			delItemNum = deleteItems(args);
//		} else {
//			delItemNum = deleteWithClause(args);
//		}
//
//		return genResult(delItemNum);
//	}
//
//	private Object genResult(int result) {
//		if (returnType == null) {
//			return null;
//		} else if (returnType.equals(Integer.class) || returnType.equals(Integer.TYPE)) {
//			return result;
//		} else {
//			return null;
//		}
//	}
//
//	private int deleteItems(Object[] args) throws IllegalAccessException, ClassParseException, InstantiationException, DataSourceException {
//		int delItemNum = 0;
//		for (Object arg : args) {
//			InstanceParseResult result = InstanceParser.parse(arg);
//			String table = result.tableName;
//			StringBuilder whereClause = new StringBuilder();
//			List<String> whereArgs = new ArrayList<>();
//
//			List<InstanceParseResult.FieldParseResult> pkFieldParseResults = new ArrayList<>();
//
//			for (int i = 0; i < result.fieldParseResults.size(); i++) {
//				InstanceParseResult.FieldParseResult r = result.fieldParseResults.get(i);
//				if (r.primaryKey) {
//					pkFieldParseResults.add(r);
//				}
//			}
//
//			for (int i = 0; i < pkFieldParseResults.size(); i++) {
//				InstanceParseResult.FieldParseResult r = pkFieldParseResults.get(i);
//				whereClause.append(r.columnName);
//				whereClause.append(" = ?");
//				if (i < pkFieldParseResults.size() - 1) {
//					whereClause.append(" AND ");
//				}
//				Object columnValue = r.columnValue;
//				if (columnValue != null)
//					whereArgs.add(columnValue.toString());
//				else
//					whereArgs.add(null);
//			}
//
//			delItemNum += executeDelete(
//					table,
//					whereClause.toString(),
//					whereArgs.toArray(MethodInvokeHelper.STRING_TEMPLATE)
//			);
//		}
//		return delItemNum;
//	}
//
//	private int deleteWithClause(Object[] args) throws IllegalAccessException, InstantiationException, DataSourceException {
//		String tableName = deleteSql.tableName();
//		if (StringUtils.isEmpty(tableName)) {
//			tableName = DeleteMethodProxy.this.tableName;
//		}
//		String whereClause = deleteSql.whereClause();
//		String[] whereArgs = MethodInvokeHelper.getWhereArgs(args, method);
//		return executeDelete(tableName, whereClause, whereArgs);
//	}
//
//	private int executeDelete(String table, String whereClause, String[] whereArgs) throws DataSourceException {
//		IExecutor executor = getExecutor();
//		Log.i(LOG_TAG, "delete, table: " + table + ", whereClause: " + whereClause + ", whereArgs: " + Arrays.toString(whereArgs));
//		return executor.executeDelete(
//				table,
//				whereClause,
//				whereArgs
//		);
//	}

}
