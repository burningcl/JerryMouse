package com.skyline.db.jerrymouse.core.log;


/**
 * Created by chenliang on 2017/3/6.
 */

public interface Logger {

	void v(String tag, String msg);

	void v(String tag, String msg, Throwable throwable);

	void d(String tag, String msg);

	void d(String tag, String msg, Throwable throwable);

	void i(String tag, String msg);

	void i(String tag, String msg, Throwable throwable);

	void w(String tag, String msg);

	void w(String tag, String msg, Throwable throwable);

	void e(String tag, String msg);

	void e(String tag, String msg, Throwable throwable);

}
