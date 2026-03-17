package com.travelbuddy.app.exceptions;

import org.springframework.http.HttpStatus;

public class CoreException extends RuntimeException {

    private final HttpStatus status;
    private final String message;
    public CoreException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
