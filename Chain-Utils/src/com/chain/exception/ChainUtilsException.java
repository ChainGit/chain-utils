package com.chain.exception;

public class ChainUtilsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChainUtilsException() {
		super();
	}

	public ChainUtilsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ChainUtilsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChainUtilsException(String message) {
		super(message);
	}

	public ChainUtilsException(Throwable cause) {
		super(cause);
	}

}
