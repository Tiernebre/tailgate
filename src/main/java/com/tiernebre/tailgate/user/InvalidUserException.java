package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.exception.InvalidException;
import lombok.Getter;

import java.util.Collection;

@Getter
public class InvalidUserException extends InvalidException {
    public InvalidUserException(
            Collection<String> errors
    ) {
        super("The request to create a user contained invalid data.", errors);
    }
}
