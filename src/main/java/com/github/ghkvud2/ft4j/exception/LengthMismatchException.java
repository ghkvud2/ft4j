package com.github.ghkvud2.ft4j.exception;

public class LengthMismatchException extends RuntimeException {

	private static final long serialVersionUID = -102449731023606392L;

	public LengthMismatchException() {
		super();
	}

	public LengthMismatchException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LengthMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public LengthMismatchException(String message) {
		super(message);
	}

	public LengthMismatchException(Throwable cause) {
		super(cause);
	}
}
