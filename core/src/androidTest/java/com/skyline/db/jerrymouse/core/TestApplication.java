package com.skyline.db.jerrymouse.core;

import android.app.Application;

/**
 * Created by jarisu on 16/1/26.
 */
public class TestApplication extends Application {

	private static final TestApplication INSTANCE = new TestApplication();

	public static TestApplication getInstance() {
		return INSTANCE;
	}

}
