package com.tiernebre.tailgate.user.exception;

import com.tiernebre.tailgate.exception.InvalidException;
import lombok.Getter;

import java.util.Set;

@Getter
public class InvalidUserException extends InvalidException {
    public InvalidUserException(
            Set<String> errors
    ) {
        super("The request to create a user contained invalid data.", errors);
    }
}
