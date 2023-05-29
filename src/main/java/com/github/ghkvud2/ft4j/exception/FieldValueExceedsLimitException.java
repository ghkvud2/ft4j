package com.github.ghkvud2.ft4j.exception;

public class FieldValueExceedsLimitException extends RuntimeException {

	private static final long serialVersionUID = 7728297301218017130L;

	public FieldValueExceedsLimitException() {
		super();
	}

	public FieldValueExceedsLimitException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FieldValueExceedsLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldValueExceedsLimitException(String message) {
		super(message);
	}

	public FieldValueExceedsLimitException(Throwable cause) {
		super(cause);
	}

}