package com.skyline.db.jerrymouse.core.notepad.util;


import com.skyline.db.jerrymouse.core.mapper.ITypeMapper;

/**
 * Created by apple on 16/1/22.
 */
public class BooleanMapper implements ITypeMapper<Boolean, Integer> {


	@Override
	public Integer mapMetaType(Boolean type) {
		return type ? 1 : 0;
	}

	@Override
	public Boolean mapDbType(Integer type) {
		return type != null && type > 0 ? true : false;
	}
}
