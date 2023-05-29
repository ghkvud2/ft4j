package com.github.ghkvud2.ft4j.exception;

public class MissingGeneratorException extends RuntimeException {

	private static final long serialVersionUID = -9067330312709208053L;

	public MissingGeneratorException() {
		super();
	}

	public MissingGeneratorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MissingGeneratorException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingGeneratorException(String message) {
		super(message);
	}

	public MissingGeneratorException(Throwable cause) {
		super(cause);
	}

}
