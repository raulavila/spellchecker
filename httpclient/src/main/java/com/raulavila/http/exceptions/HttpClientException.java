package com.raulavila.http.exceptions;

public class HttpClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}

