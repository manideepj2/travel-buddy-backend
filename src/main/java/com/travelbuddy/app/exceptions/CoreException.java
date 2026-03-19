package com.travelbuddy.app.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CoreException extends RuntimeException {

    private final HttpStatus status;
    private final String message;
    public CoreException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }
}
