package com.sharesphere.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends ShareSphereException {
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
