package com.skyline.db.jerrymouse.core.annotation;

import com.skyline.db.jerrymouse.core.mapper.IOrMapper;
import com.skyline.db.jerrymouse.core.mapper.MapperNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jairus on 15-12-10.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapper {

	/**
	 * If raw is true, the mapper would not work, return the {@link android.database.Cursor} directly.
	 *
	 * @return
	 */
	boolean raw() default false;

	/**
	 * the class of the mapper which implements {@link IOrMapper }.
	 *
	 * @return
	 */
	Class<? extends IOrMapper> mapper() default MapperNull.class;
}
