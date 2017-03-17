package com.skyline.db.jerrymouse.core.bytecode;

import android.test.AndroidTestCase;
import android.util.Log;

import com.skyline.db.jerrymouse.core.datasource.DataSourceHolder;
import com.skyline.db.jerrymouse.core.log.LogUtil;
import com.skyline.db.jerrymouse.core.log.Logger;
import com.skyline.db.jerrymouse.core.mapper.ormapper.ByteCodeMapper;
import com.skyline.db.jerrymouse.core.notepad.meta.Note;

import org.junit.Test;

/**
 * Created by chenliang on 2017/3/13.
 */

public class ByteCodeMapperTest extends AndroidTestCase {

	public static final String LOG_TAG = "ByteCodeMapperTest";

	@Test
	public void test() {
		DataSourceHolder.CONTEXT = getContext();
		//Log.d(LOG_TAG, ByteCodeMapperTest.class.getPackage().getName());
		try {
			ByteCodeMapper.getInstance(Note.class);
			LogUtil.setLogger(new Logger() {
				@Override
				public void v(String tag, String msg) {
					Log.i(tag, msg);
				}

				@Override
				public void v(String tag, String msg, Throwable throwable) {
					Log.i(tag, msg);
				}

				@Override
				public void d(String tag, String msg) {
					Log.i(tag, msg);
				}

				@Override
				public void d(String tag, String msg, Throwable throwable) {
					Log.i(tag, msg);
				}

				@Override
				public void i(String tag, String msg) {
					Log.i(tag, msg);
				}

				@Override
				public void i(String tag, String msg, Throwable throwable) {
					Log.i(tag, msg);
				}

				@Override
				public void w(String tag, String msg) {
					Log.i(tag, msg);
				}

				@Override
				public void w(String tag, String msg, Throwable throwable) {
					Log.i(tag, msg);
				}

				@Override
				public void e(String tag, String msg) {
					Log.i(tag, msg);
				}

				@Override
				public void e(String tag, String msg, Throwable throwable) {
					Log.i(tag, msg);
				}
			});
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
