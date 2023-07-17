package com.github.ghkvud2.ft4j.exception;

public class MultipleAnnotationException extends RuntimeException {

	private static final long serialVersionUID = -4608513657745348474L;

	public MultipleAnnotationException() {
		super();
	}

	public MultipleAnnotationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MultipleAnnotationException(String message, Throwable cause) {
		super(message, cause);
	}

	public MultipleAnnotationException(String message) {
		super(message);
	}

	public MultipleAnnotationException(Throwable cause) {
		super(cause);
	}

}
