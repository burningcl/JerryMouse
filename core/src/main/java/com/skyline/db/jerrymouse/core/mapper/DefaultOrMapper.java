package com.skyline.db.jerrymouse.core.mapper;

import android.database.Cursor;
import android.util.Log;
import android.util.Pair;

import com.skyline.db.jerrymouse.core.LRUCache;
import com.skyline.db.jerrymouse.core.annotation.DbField;
import com.skyline.db.jerrymouse.core.util.TypeMapperHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by jairus on 16/1/20.
 */
public class DefaultOrMapper<T> extends AbsOrMapper<T> implements IOrMapper<T> {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = DefaultOrMapper.class.getSimpleName();

	public static LRUCache<Class, DefaultOrMapper> CACHE = new LRUCache<>(32);

	private Class<? extends T> clazz;

	private DbField[] dbFields;

	private Field[] fields;

	private DefaultOrMapper(Class<? extends T> clazz) {
		this.clazz = clazz;
		fields = clazz.getFields();
		dbFields = new DbField[fields.length];
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			dbFields[i] = field.getAnnotation(DbField.class);
		}
	}

	public static IOrMapper getInstance(Class clazz) {
		if (clazz == null) {
			Log.w(LOG_TAG, "getInstance, clazz is null, pls check");
			return null;
		}
		DefaultOrMapper mapper = CACHE.get(clazz);
		if (mapper != null) {
			return mapper;
		}
		synchronized (CACHE) {
			mapper = CACHE.get(clazz);
			if (mapper != null) {
				return mapper;
			}
			mapper = new DefaultOrMapper(clazz);
			CACHE.put(clazz, mapper);
		}
		return mapper;
	}

	@Override
	public T map(Cursor c) throws IllegalAccessException, InstantiationException {
		if (c == null || !c.moveToNext()) {
			return null;
		}
		Object obj = clazz.newInstance();
		if (fields == null || fields.length <= 0) {
			return (T) obj;
		}

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			//this optimization is really very important, it will improve the performance by about 300%.
			DbField dbField = dbFields[i];
			if (dbField == null) {
				continue;
			}
			String columnName = dbField.name();
			// if the column name is not setted,the column name is just the field name;
			if (columnName == null || columnName.length() <= 0) {
				columnName = field.getName();
			}
			ITypeMapper typeMapper = null;
			Pair<Type, Type> typePair = null;
			Class<? extends ITypeMapper> typeMapperClass = dbField.mapper();
			if (typeMapperClass != MapperNull.class) {
				typeMapper = TypeMapperHelper.getInstance(typeMapperClass);
				typePair = TypeMapperHelper.getMapperGenericType(typeMapperClass);
			}

			Object value = null;
			if (typeMapper == null || typePair == null) {
				value = getFieldValue(c, columnName, field.getType());
			} else {
				Object dbValue = getFieldValue(c, columnName, typePair.second);
				if (dbValue != null) {
					value = typeMapper.mapDbType(dbValue);
				}
			}
			if (value != null) {
				boolean isAccessible = field.isAccessible();
				field.setAccessible(true);
				field.set(obj, value);
				field.setAccessible(isAccessible);
			}
		}
		return (T) obj;
	}

	private Object getFieldValue(Cursor cursor, String columnName, Type type) {
		if (cursor == null || columnName == null || type == null) {
			Log.w(LOG_TAG, "getFieldValue, fail, cursor, columnName or type is null");
			return null;
		}
		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex < 0) {
			Log.w(LOG_TAG, "getFieldValue, fail, could not found columnIndex for " + columnName);
			return null;
		}
		return super.getFieldValue(cursor, columnIndex, type);
	}

}
