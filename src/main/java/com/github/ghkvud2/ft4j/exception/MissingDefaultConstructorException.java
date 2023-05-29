package com.github.ghkvud2.ft4j.exception;

public class MissingDefaultConstructorException extends RuntimeException {

	private static final long serialVersionUID = -7067543253474359088L;

	public MissingDefaultConstructorException() {
		super();
	}

	public MissingDefaultConstructorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MissingDefaultConstructorException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingDefaultConstructorException(String message) {
		super(message);
	}

	public MissingDefaultConstructorException(Throwable cause) {
		super(cause);
	}

}
