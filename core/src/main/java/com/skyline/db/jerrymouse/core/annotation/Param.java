package com.skyline.db.jerrymouse.core.annotation;

import com.skyline.db.jerrymouse.core.mapper.MapperNull;
import com.skyline.db.jerrymouse.core.mapper.ITypeMapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jairus on 15-12-10.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

	/**
	 * @return
	 */
	Class<? extends ITypeMapper> mapper() default MapperNull.class;

}
