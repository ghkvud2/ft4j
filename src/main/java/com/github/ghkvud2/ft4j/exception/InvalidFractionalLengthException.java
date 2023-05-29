package com.github.ghkvud2.ft4j.exception;

public class InvalidFractionalLengthException extends RuntimeException {

	private static final long serialVersionUID = -6814194934865288178L;

	public InvalidFractionalLengthException() {
		super();
	}

	public InvalidFractionalLengthException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidFractionalLengthException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidFractionalLengthException(String message) {
		super(message);
	}

	public InvalidFractionalLengthException(Throwable cause) {
		super(cause);
	}

}