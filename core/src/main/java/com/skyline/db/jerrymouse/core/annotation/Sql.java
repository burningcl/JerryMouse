package com.skyline.db.jerrymouse.core.annotation;

import com.skyline.db.jerrymouse.core.util.StringUtils;
import com.skyline.db.jerrymouse.core.type.SqlType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jairus on 15-12-10.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sql {

	/**
	 * @return
	 */
	SqlType type();

	/**
	 * @return
	 */
	SelectSql select() default @SelectSql(sql = StringUtils.EMPTY_STR);

	/**
	 * @return
	 */
	UpdateSql update() default @UpdateSql(tableName = StringUtils.EMPTY_STR, whereClause = StringUtils.EMPTY_STR);

	/**
	 * @return
	 */
	DeleteSql delete() default @DeleteSql(tableName = StringUtils.EMPTY_STR, whereClause = StringUtils.EMPTY_STR);
}