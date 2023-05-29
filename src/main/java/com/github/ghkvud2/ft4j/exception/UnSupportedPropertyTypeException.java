package com.github.ghkvud2.ft4j.exception;

public class UnSupportedPropertyTypeException extends RuntimeException {

	private static final long serialVersionUID = -6814194934865288178L;

	public UnSupportedPropertyTypeException() {
		super();
	}

	public UnSupportedPropertyTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnSupportedPropertyTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnSupportedPropertyTypeException(String message) {
		super(message);
	}

	public UnSupportedPropertyTypeException(Throwable cause) {
		super(cause);
	}

}