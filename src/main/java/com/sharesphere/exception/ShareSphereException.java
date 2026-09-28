package com.sharesphere.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ShareSphereException extends RuntimeException {
    private final HttpStatus status;

    public ShareSphereException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
