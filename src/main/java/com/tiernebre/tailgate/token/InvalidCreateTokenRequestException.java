package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.exception.InvalidException;

import java.util.Collection;

public class InvalidCreateTokenRequestException extends InvalidException {
    public InvalidCreateTokenRequestException(
            Collection<String> errors
    ) {
        super("The request to create a token contained invalid data.", errors);
    }
}
