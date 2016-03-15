package com.skyline.db.jerrymouse.core.exception;

/**
 * Created by jairus on 15-4-3.
 */
public class DataSourceException extends Exception {

	private Reason reason;

	public DataSourceException(Reason reason) {
		super(reason.toString());
	}

	public DataSourceException(Reason reason, Throwable throwable) {
		super(reason.toString(), throwable);
	}

	public Reason getReason() {
		return reason;
	}

	public static enum Reason {

		DATA_SOURCE_ALREADY_INITED("data source already has been initialized!"),

		DATA_SOURCE_DOES_NOT_MATCH("data source does not match!"),

		DATA_SOURCE_NOT_INITED("data source has not been initialized!"),

		DATA_SOURCE_INIT_FAIL("data source fail to be initialized!"),

		DAO_CLASS_REQUIRED("");

		protected String msg;

		Reason(String msg) {
			this.msg = msg;
		}

		@Override
		public String toString() {
			return super.toString() + ":" + msg;
		}
	}
}

