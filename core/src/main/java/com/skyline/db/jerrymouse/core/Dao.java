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
	long[] insert(Meta... meta);

	@Sql(type = SqlType.DELETE)
	long delete(Meta... meta);

	@Sql(type = SqlType.UPDATE)
	long update(Meta... meta);

}
