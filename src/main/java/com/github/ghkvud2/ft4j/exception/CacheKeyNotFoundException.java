package com.github.ghkvud2.ft4j.exception;

public class CacheKeyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3464077510847955789L;

	public CacheKeyNotFoundException() {
		super();
	}

	public CacheKeyNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CacheKeyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheKeyNotFoundException(String message) {
		super(message);
	}

	public CacheKeyNotFoundException(Throwable cause) {
		super(cause);
	}

}