package com.esed.payer.archiviocarichi.webservice.exception;

public class DuplicateException extends Exception {
	
	/**
	 * Constructor of DuplicateException.
	 * @param message - The message that we pass in case of this exception.
	 * @param cause - The {@link Throwable} class is the superclass of all errors and exceptions in the Java language.
	 */
	public DuplicateException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Constructor of DuplicateException.
	 * @param message - The message that we pass in case of this exception.
	 */
	public DuplicateException(String message) {
		super(message);
	}
	/**
	 * Constructor of DuplicateException.
	 * @param cause - The {@link Throwable} class is the superclass of all errors and exceptions in the Java language.
	 */
	public DuplicateException(Throwable cause) {
		super(cause);
	}

}
