package com.skyline.db.jerrymouse.core;

import com.skyline.db.jerrymouse.core.annotation.Sql;
import com.skyline.db.jerrymouse.core.type.SqlType;

/**
 * Created by jairus on 15-12-10.
 *
 * @param <Meta>
 */
public interface Dao<Meta> {

	@Sql(type = SqlType.INSERT)
	long add(Meta meta);

	@Sql(type = SqlType.DELETE)
	int delete(Meta... meta);

	@Sql(type = SqlType.UPDATE)
	int update(Meta... meta);

}
