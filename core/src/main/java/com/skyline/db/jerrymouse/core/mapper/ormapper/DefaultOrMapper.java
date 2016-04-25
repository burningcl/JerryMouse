package com.skyline.db.jerrymouse.core.mapper.ormapper;

import android.database.Cursor;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;

import com.skyline.db.jerrymouse.core.annotation.DbField;
import com.skyline.db.jerrymouse.core.mapper.MapperNull;
import com.skyline.db.jerrymouse.core.mapper.typemapper.ITypeMapper;
import com.skyline.db.jerrymouse.core.util.StringUtils;
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

	public static LruCache<Class, DefaultOrMapper> CACHE = new LruCache<>(32);

	private Class<? extends T> clazz;

	private DbField[] dbFields;

	private Field[] fields;

	private int[] columnIndexes;

	private Type[] columnTypes;

	private Class<?>[] mapperClasses;

	private DefaultOrMapper(Class<? extends T> clazz) throws InstantiationException, IllegalAccessException {
		this.clazz = clazz;
		fields = clazz.getFields();
		int size = fields.length;
		dbFields = new DbField[size];
		columnIndexes = new int[size];
		columnTypes = new Type[size];
		mapperClasses = new Class<?>[size];

		for (int i = 0; i < size; i++) {
			Field field = fields[i];
			dbFields[i] = field.getAnnotation(DbField.class);

			if (dbFields[i] == null) {
				continue;
			}
			field.setAccessible(true);
			columnIndexes[i] = -1;

			Class<? extends ITypeMapper> typeMapperClass = dbFields[i].mapper();
			if (typeMapperClass != MapperNull.class) {
				Pair<Type, Type> typePair = TypeMapperHelper.getMapperGenericType(typeMapperClass);
				mapperClasses[i] = typeMapperClass;
				columnTypes[i] = typePair.second;
			} else {
				columnTypes[i] = field.getType();
			}
		}
	}

	public static IOrMapper getInstance(Class clazz) throws IllegalAccessException, InstantiationException {
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

			int columnIndex = getColumnIndex(c, dbField, field, i);

			//Log.d(LOG_TAG, "dbField: " + dbField + ", field: " + field + ", columnIndex: " + columnIndex);

			Type columnType = columnTypes[i];
			Class<? extends ITypeMapper> typeMapperClass = (Class<? extends ITypeMapper>) mapperClasses[i];

			Object value = getFieldValue(c, columnIndex, columnType);
			if (typeMapperClass != null && value != null) {
				value = TypeMapperHelper.getInstance(typeMapperClass).mapDbType(value);
			}
			if (value != null) {
				field.set(obj, value);
			}
		}
		return (T) obj;
	}

	private int getColumnIndex(Cursor cursor, DbField dbField, Field field, int fieldIndex) {
		if (cursor == null || dbField == null || field == null || fieldIndex < 0) {
			return -1;
		}
		int columnIndex = columnIndexes[fieldIndex];
		if (columnIndex >= 0) {
			return columnIndex;
		}
		String columnName = dbField.name();
		// if the column name is not setted,the column name is just the field name;
		if (StringUtils.isEmpty(columnName)) {
			columnName = field.getName();
		}
		columnIndex = cursor.getColumnIndex(columnName);
		columnIndexes[fieldIndex] = columnIndex;
		return columnIndex;

	}
}
