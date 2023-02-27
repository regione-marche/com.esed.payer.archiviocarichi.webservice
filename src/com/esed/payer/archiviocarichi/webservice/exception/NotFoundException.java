package com.esed.payer.archiviocarichi.webservice.exception;

public class NotFoundException extends Exception {
	
	/**
	 * Constructor of NotFoundException.
	 * @param message - The message that we pass in case of this exception.
	 * @param cause - The {@link Throwable} class is the superclass of all errors and exceptions in the Java language.
	 */
	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Constructor of NotFoundException.
	 * @param message - The message that we pass in case of this exception.
	 */
	public NotFoundException(String message) {
		super(message);
	}
	/**
	 * Constructor of NotFoundException.
	 * @param cause - The {@link Throwable} class is the superclass of all errors and exceptions in the Java language.
	 */
	public NotFoundException(Throwable cause) {
		super(cause);
	}

}
