package com.github.ghkvud2.ft4j.exception;

public class FieldValueUnderFlowException extends RuntimeException {

	private static final long serialVersionUID = -6895297075903716815L;

	public FieldValueUnderFlowException() {
		super();
	}

	public FieldValueUnderFlowException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FieldValueUnderFlowException(String message, Throwable cause) {
		super(message, cause);
	}

	public FieldValueUnderFlowException(String message) {
		super(message);
	}

	public FieldValueUnderFlowException(Throwable cause) {
		super(cause);
	}

}