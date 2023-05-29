package com.github.ghkvud2.ft4j.exception;

public class DefaultValueExceedsLimitException extends RuntimeException {

	private static final long serialVersionUID = 1809945913686757475L;

	public DefaultValueExceedsLimitException() {
		super();
	}

	public DefaultValueExceedsLimitException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DefaultValueExceedsLimitException(String message, Throwable cause) {
		super(message, cause);
	}

	public DefaultValueExceedsLimitException(String message) {
		super(message);
	}

	public DefaultValueExceedsLimitException(Throwable cause) {
		super(cause);
	}

}