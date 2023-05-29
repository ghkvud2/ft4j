package com.github.ghkvud2.ft4j.exception;

public class MissingOrderAnnotationException extends RuntimeException {

	private static final long serialVersionUID = 7302628365160172830L;

	public MissingOrderAnnotationException() {
		super();
	}

	public MissingOrderAnnotationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MissingOrderAnnotationException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingOrderAnnotationException(String message) {
		super(message);
	}

	public MissingOrderAnnotationException(Throwable cause) {
		super(cause);
	}

}
