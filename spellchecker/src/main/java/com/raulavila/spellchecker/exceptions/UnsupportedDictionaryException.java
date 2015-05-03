package com.raulavila.spellchecker.exceptions;

public class UnsupportedDictionaryException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnsupportedDictionaryException(String message) {
		super(message);
	}
	
	public UnsupportedDictionaryException(String message, Throwable e) {
		super(message, e);
	}
	
}
