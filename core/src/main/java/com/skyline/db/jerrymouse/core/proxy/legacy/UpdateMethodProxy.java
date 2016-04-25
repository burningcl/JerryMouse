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
public class UpdateMethodProxy extends AbsMethodProxy {
	public UpdateMethodProxy(Class<? extends Dao> clazz, Method method) {
		super(clazz, method);
	}

	@Override
	public Object invoke(Object[] args) throws InstantiationException, IllegalAccessException, DataSourceException, ClassParseException, NoSuchMethodException, InvocationTargetException, SQLException {
		return null;
	}

//	/**
//	 * LOG_TAG
//	 */
//	private static final String LOG_TAG = UpdateMethodProxy.class.getSimpleName();
//
//	//private UpdateSql updateSql;
//
//	public UpdateMethodProxy(Class<? extends Dao> clazz, Method method, UpdateSql updateSql) throws MethodParseException {
//		super(clazz, method);
//		if (updateSql == null) {
//			throw new MethodParseException(MethodParseException.Reason.UPDATE_SQL_REQUIRED);
//		}
//		this.updateSql = updateSql;
//	}
//
//	@Override
//	public synchronized void parseClassAnnotations() throws ClassParseException {
//		super.parseClassAnnotations();
//	}
//
//	@Override
//	public synchronized void parseMethodAnnotations() throws NoSuchMethodException, MethodParseException, InstantiationException, IllegalAccessException {
//		super.parseMethodAnnotations();
//	}
//
//	@Override
//	public Object invoke(Object[] args) throws IllegalAccessException, ClassParseException, InstantiationException, DataSourceException {
//
//		int updateItemNum = 0;
//
//		if (args == null || args.length <= 0) {
//			Log.w(LOG_TAG, "invoke, fail, args is empty!");
//			return genResult(updateItemNum);
//		}
//
//		ArgsType argsType = getArgsType(args);
//
//		if (argsType == ArgsType.ILLEGAL) {
//			updateItemNum = 0;
//		} else if (argsType == ArgsType.ARRAY) {
//			updateItemNum = updateItems((Object[]) args[0]);
//		} else if (argsType == ArgsType.SINGLE) {
//			updateItemNum = updateItems(args);
//		} else {
//			updateItemNum = updateWithClause(args);
//		}
//
//		return genResult(updateItemNum);
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
//	private int updateItems(Object[] args) throws DataSourceException, IllegalAccessException, ClassParseException, InstantiationException {
//
//		int updateItemNum = 0;
//
//		for (Object arg : args) {
//			InstanceParseResult result = InstanceParser.parse(arg);
//			String table = result.tableName;
//			ContentValues values = new ContentValues();
//			StringBuilder whereClause = new StringBuilder();
//			List<String> whereArgs = new ArrayList<>();
//
//			List<InstanceParseResult.FieldParseResult> pkFieldParseResults = new ArrayList<>();
//
//			for (int i = 0; i < result.fieldParseResults.size(); i++) {
//				InstanceParseResult.FieldParseResult r = result.fieldParseResults.get(i);
//				if (r.primaryKey) {
//					pkFieldParseResults.add(r);
//				} else {
//					ContentValuesHelper.put(r.columnName, r.columnValue, values);
//				}
//			}
//
//			for (int i = 0; i < pkFieldParseResults.size(); i++) {
//				InstanceParseResult.FieldParseResult r = pkFieldParseResults.get(i);
//				whereClause.append(r.columnName);
//				whereClause.append(" = ?");
//				if (i < pkFieldParseResults.size() - 1) {
//					whereClause.append(", ");
//				}
//				Object columnValue = r.columnValue;
//				if (columnValue != null)
//					whereArgs.add(columnValue.toString());
//				else
//					whereArgs.add(null);
//			}
//
//			updateItemNum += executeUpdate(
//					table,
//					values,
//					whereClause.toString(),
//					whereArgs.toArray(MethodInvokeHelper.STRING_TEMPLATE)
//			);
//		}
//		return updateItemNum;
//	}
//
//	private int updateWithClause(Object[] args) throws IllegalAccessException, InstantiationException, DataSourceException {
//		String tableName = updateSql.tableName();
//		if (StringUtils.isEmpty(tableName)) {
//			tableName = UpdateMethodProxy.this.tableName;
//		}
//		ContentValues values = MethodInvokeHelper.getValueArgs(args, method);
//		String whereClause = updateSql.whereClause();
//		String[] whereArgs = MethodInvokeHelper.getWhereArgs(args, method);
//		return executeUpdate(tableName, values, whereClause, whereArgs);
//	}
//
//	private int executeUpdate(String table, ContentValues values, String whereClause, String[] whereArgs) throws DataSourceException {
//		IExecutor executor = getExecutor();
//		Log.i(LOG_TAG, "update, table: " + table + ", values: " + values + ", whereClause: " + whereClause + ", whereArgs: " + Arrays.toString(whereArgs));
//		return executor.executeUpdate(
//				table,
//				values,
//				whereClause,
//				whereArgs
//		);
//
//	}

}
