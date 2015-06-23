package com.raulavila.spellchecker.exceptions;

public class MissingArgumentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MissingArgumentException(String message) {
        super(message);
    }

    public MissingArgumentException(String message, Throwable e) {
        super(message, e);
    }

}
