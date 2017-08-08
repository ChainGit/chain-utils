package com.chain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChainUtilsRuntimeException extends RuntimeException {

	private static final Logger logger = LoggerFactory.getLogger(ChainUtilsRuntimeException.class);

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
		logger.warn(message, cause);
	}

	public ChainUtilsRuntimeException(String message, Throwable cause) {
		super(message, cause);
		logger.warn(message, cause);
	}

	public ChainUtilsRuntimeException(String message) {
		super(message);
		logger.warn(message);
	}

	public ChainUtilsRuntimeException(Throwable cause) {
		super(cause);
	}

}
