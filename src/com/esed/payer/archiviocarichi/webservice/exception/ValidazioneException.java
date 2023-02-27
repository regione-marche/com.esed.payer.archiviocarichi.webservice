package com.esed.payer.archiviocarichi.webservice.exception;

public class ValidazioneException extends Exception {
	
	/**
	 * Constructor of ValidazioneException.
	 * @param message - The message that we pass in case of this exception.
	 * @param cause - The {@link Throwable} class is the superclass of all errors and exceptions in the Java language.
	 */
	public ValidazioneException(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Constructor of ValidazioneException.
	 * @param message - The message that we pass in case of this exception.
	 */
	public ValidazioneException(String message) {
		super(message);
	}
	/**
	 * Constructor of ValidazioneException.
	 * @param cause - The {@link Throwable} class is the superclass of all errors and exceptions in the Java language.
	 */
	public ValidazioneException(Throwable cause) {
		super(cause);
	}

}
