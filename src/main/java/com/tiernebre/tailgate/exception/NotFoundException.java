package com.tiernebre.tailgate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public abstract class NotFoundException extends Exception {
    protected NotFoundException(String message) {
        super(message);
    }
}
