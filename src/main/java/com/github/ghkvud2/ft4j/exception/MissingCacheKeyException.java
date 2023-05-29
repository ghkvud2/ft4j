package com.github.ghkvud2.ft4j.exception;

public class MissingCacheKeyException extends RuntimeException {

	private static final long serialVersionUID = 7767811032471047927L;

	public MissingCacheKeyException() {
		super();
	}

	public MissingCacheKeyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MissingCacheKeyException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingCacheKeyException(String message) {
		super(message);
	}

	public MissingCacheKeyException(Throwable cause) {
		super(cause);
	}

}
