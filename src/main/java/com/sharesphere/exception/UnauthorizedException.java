package com.sharesphere.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ShareSphereException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
