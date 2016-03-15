package com.skyline.db.jerrymouse.core.annotation;

import com.skyline.db.jerrymouse.core.mapper.MapperNull;
import com.skyline.db.jerrymouse.core.mapper.ITypeMapper;
import com.skyline.db.jerrymouse.core.type.SortType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jairus on 15-12-10.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbField {

	/**
	 * column name
	 *
	 * if defaultValue is setted, the field name in class is different from column name in the db.
	 *
	 * @return
	 */
	String name() default "";

	/**
	 * default value
	 *
	 * if you need to set defaultValue, please check the data type of this column.
	 *
	 * @return
	 */
	String defaultValue() default "";

	/**
	 * index type
	 * 1.null: There is no index on this column;
	 * 2.{@link com.skyline.db.jerrymouse.core.type.SortType}.ASC;
	 * 3.{@link com.skyline.db.jerrymouse.core.type.SortType}.DESC;
	 *
	 * @return
	 */
	SortType index() default SortType.NULL;

	/**
	 * Does this column haves an unique index.
	 *
	 * @return
	 */
	boolean unique() default false;

	/**
	 * Is it allowed to set a null value to this column?
	 *
	 * @return
	 */
	boolean notNull() default false;

	/**
	 * If the type of the column in db is different from the type of the field in class,
	 * You can use the {@link ITypeMapper} to map.
	 * The field data type in the class in
	 * [ Long, long, Integer integer, Character, char, Byte, byte, Short, short, Double, double, Float, float, String ],
	 * can be mapped to the data type allowed to the column {@link com.skyline.db.jerrymouse.core.type.DbColumnType}.
	 *
	 * @return
	 */
	Class<? extends ITypeMapper> mapper() default MapperNull.class;

	/**
	 * primary key
	 *
	 * @return
	 */
	PrimaryKey primaryKey() default @PrimaryKey(primaryKey = false, autoIncrement = false);

}
