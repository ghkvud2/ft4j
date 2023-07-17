package com.github.ghkvud2.ft4j.exception;

@Deprecated
public class InvalidAnnotationConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 7483377571270079324L;

	public InvalidAnnotationConfigurationException() {
		super();
	}

	public InvalidAnnotationConfigurationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidAnnotationConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAnnotationConfigurationException(String message) {
		super(message);
	}

	public InvalidAnnotationConfigurationException(Throwable cause) {
		super(cause);
	}

}