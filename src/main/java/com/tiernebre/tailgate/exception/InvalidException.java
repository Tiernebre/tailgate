package com.tiernebre.tailgate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collection;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public abstract class InvalidException extends Exception {
    private final Collection<String> errors;

    protected InvalidException(
            String message,
            Collection<String> errors
    ) {
        super(message);
        this.errors = errors;
    }
}
