package com.sharesphere.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ShareSphereException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
