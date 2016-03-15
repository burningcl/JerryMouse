package com.skyline.db.jerrymouse.core.exception;

/**
 * Created by jairus on 15/12/22.
 */
public class ClassParseException extends Exception {

	private Reason reason;

	public ClassParseException(Reason reason) {
		super(reason.toString());
	}

	public ClassParseException(Reason reason, Throwable throwable) {
		super(reason.toString(), throwable);
	}

	public static enum Reason {

		DB_TABLE_ANNOTATION_REQUIRED("the annotation DbTable is required!"),

		;

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
