package com.skyline.db.jerrymouse.core.mapper;

/**
 * Created by jairus on 15/12/21.
 */
public interface ITypeMapper<M, D> {

	/**
	 * @param type the type of the field in the meta Object
	 * @return
	 */
	D mapMetaType(M type);

	/**
	 * @param type the type of the column in the table
	 * @return
	 */
	M mapDbType(D type);

}
