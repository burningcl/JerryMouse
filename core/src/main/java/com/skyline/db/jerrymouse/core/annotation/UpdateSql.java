package com.skyline.db.jerrymouse.core.annotation;

import com.skyline.db.jerrymouse.core.util.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jairus on 15-12-10.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateSql {

	/**
	 * @return
	 */
	String tableName() default StringUtils.EMPTY_STR;

	/**
	 * @return
	 */
	String whereClause();
}