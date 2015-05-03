package com.raulavila.spchclient.exceptions;

public class ServerErrorException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServerErrorException(String message) {
		super(message);
	}
	
	public ServerErrorException(String message, Throwable e) {
		super(message, e);
	}
	
}
