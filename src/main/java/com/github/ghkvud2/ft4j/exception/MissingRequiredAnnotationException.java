package com.github.ghkvud2.ft4j.exception;

public class MissingRequiredAnnotationException extends RuntimeException {

	private static final long serialVersionUID = 1434237863139744138L;

	public MissingRequiredAnnotationException() {
		super();
	}

	public MissingRequiredAnnotationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MissingRequiredAnnotationException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingRequiredAnnotationException(String message) {
		super(message);
	}

	public MissingRequiredAnnotationException(Throwable cause) {
		super(cause);
	}

}
