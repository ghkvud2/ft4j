package com.github.ghkvud2.ft4j.exception;

public class ClientIPGenerationException extends RuntimeException {

	private static final long serialVersionUID = -6814194934865288178L;

	public ClientIPGenerationException() {
		super();
	}

	public ClientIPGenerationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClientIPGenerationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientIPGenerationException(String message) {
		super(message);
	}

	public ClientIPGenerationException(Throwable cause) {
		super(cause);
	}

}