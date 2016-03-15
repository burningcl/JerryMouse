package com.skyline.db.jerrymouse.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jairus on 16/1/20.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey {

	/**
	 * primary key
	 *
	 * @return
	 */
	boolean primaryKey() default false;

	/**
	 * Does the primary key need to be autoIncremented
	 * @return
	 */
	boolean autoIncrement() default false;

}
