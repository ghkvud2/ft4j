package com.github.ghkvud2.ft4j.exception;

public class UnSupportedTypeException extends RuntimeException {

	private static final long serialVersionUID = -8760885524352630676L;

	public UnSupportedTypeException() {
		super();
	}

	public UnSupportedTypeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnSupportedTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnSupportedTypeException(String message) {
		super(message);
	}

	public UnSupportedTypeException(Throwable cause) {
		super(cause);
	}

}
