package com.skyline.db.jerrymouse.core.util;

import android.util.Log;
import android.util.Pair;

import com.skyline.db.jerrymouse.core.notepad.util.BooleanMapper;

import junit.framework.TestCase;

import org.junit.Test;

import java.lang.reflect.Type;

/**
 * Created by jairus on 15/12/21.
 */
public class TypeMapperHelperTest extends TestCase {

	private static final String LOG_TAG = TypeMapperHelperTest.class.getSimpleName();

	@Test
	public void test() throws Throwable {

		Pair<Type, Type> pair = TypeMapperHelper.getMapperGenericType(BooleanMapper.class);
		if (pair == null) {
			Log.d(LOG_TAG, "pair is null");
			return;
		}
		Log.d(LOG_TAG, "first: " + pair.first + "\t" + pair.first.equals(Boolean.class));
		Log.d(LOG_TAG, "second: " + pair.second + "\t" + pair.second.equals(Integer.class));
	}

}