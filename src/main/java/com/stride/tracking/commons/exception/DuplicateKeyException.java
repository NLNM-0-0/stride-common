package com.stride.tracking.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateKeyException extends RuntimeException {

    public DuplicateKeyException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s with %s '%s' already exists.", resourceName, fieldName, fieldValue));
    }

    public DuplicateKeyException(String message) {
        super(message);
    }
}
