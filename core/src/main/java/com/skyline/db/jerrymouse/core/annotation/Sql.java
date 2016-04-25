package com.skyline.db.jerrymouse.core.annotation;

import com.skyline.db.jerrymouse.core.mapper.MapperNull;
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

	String value() default "";

	/**
	 * @return
	 */
	Mapper mapper() default @Mapper(raw = false, mapper = MapperNull.class);
}