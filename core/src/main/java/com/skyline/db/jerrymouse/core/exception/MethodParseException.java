package com.skyline.db.jerrymouse.core.exception;

/**
 * Created by jairus on 15/12/22.
 */
public class MethodParseException extends Exception {

	private Reason reason;

	public MethodParseException(Reason reason) {
		super(reason.toString());
	}

	public MethodParseException(Reason reason, Throwable throwable) {
		super(reason.toString(), throwable);
	}

	public static enum Reason {

		SQL_METHOD_ANNOTATION_REQUIRED("the annotation SqlMethod is required!"),

		SELECT_SQL_REQUIRED("the annotation SelectSql is required in annotation SqlMethod!"),

		UNABLE_TO_PARSE_SQL_ANNOTATION("unable to parse sql annotation!"),

		MAPPER_DOES_NOT_MATCH("the mapper in Sql annotation does not match the return type of this method!");

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
