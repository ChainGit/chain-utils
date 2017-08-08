package com.chain.exception;

public class ChainUtilsRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChainUtilsRuntimeException() {
		super();
	}

	public ChainUtilsRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ChainUtilsRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChainUtilsRuntimeException(String message) {
		super(message);
	}

	public ChainUtilsRuntimeException(Throwable cause) {
		super(cause);
	}

}
