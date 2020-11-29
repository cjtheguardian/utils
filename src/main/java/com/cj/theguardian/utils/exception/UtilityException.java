package com.cj.theguardian.utils.exception;

public class UtilityException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4900880305462345928L;

	public UtilityException(String message) {
		super(message);
	}

	public UtilityException(String message, Throwable e) {
		super(message, e);
	}

	public UtilityException(Throwable e) {
		super(e);
	}

}
