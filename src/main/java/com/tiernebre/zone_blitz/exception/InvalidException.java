package com.tiernebre.zone_blitz.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public abstract class InvalidException extends RuntimeException {
    private final Set<String> errors;

    protected InvalidException(
            String message,
            Set<String> errors
    ) {
        super(message);
        this.errors = errors;
    }
}
