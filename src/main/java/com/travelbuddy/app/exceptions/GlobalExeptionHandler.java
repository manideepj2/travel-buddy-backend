package com.travelbuddy.app.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExeptionHandler {

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<?> handleCoreException(CoreException ex){
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }
}
