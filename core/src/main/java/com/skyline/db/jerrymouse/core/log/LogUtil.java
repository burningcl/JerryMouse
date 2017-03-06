package com.skyline.db.jerrymouse.core.log;

/**
 * Created by chenliang on 2017/3/6.
 */

public class LogUtil {

	public static Logger logger;

	public static void setLogger(Logger logger) {
		LogUtil.logger = logger;
	}

	public static void v(String tag, String msg) {
		if (logger == null) {
			return;
		}
		logger.v(tag, msg);
	}

	public static void v(String tag, String msg, Throwable throwable) {
		if (logger == null) {
			return;
		}
		logger.v(tag, msg, throwable);
	}

	public static void d(String tag, String msg) {
		if (logger == null) {
			return;
		}
		logger.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable throwable) {
		if (logger == null) {
			return;
		}
		logger.d(tag, msg, throwable);
	}

	public static void i(String tag, String msg) {
		if (logger == null) {
			return;
		}
		logger.i(tag, msg);
	}

	public static void i(String tag, String msg, Throwable throwable) {
		if (logger == null) {
			return;
		}
		logger.i(tag, msg, throwable);
	}

	public static void w(String tag, String msg) {
		if (logger == null) {
			return;
		}
		logger.w(tag, msg);
	}

	public static void w(String tag, String msg, Throwable throwable) {
		if (logger == null) {
			return;
		}
		logger.w(tag, msg, throwable);
	}

	public static void e(String tag, String msg) {
		if (logger == null) {
			return;
		}
		logger.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable throwable) {
		if (logger == null) {
			return;
		}
		logger.e(tag, msg, throwable);
	}
}
