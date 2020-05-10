package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.exception.InvalidException;

import java.util.Collection;

public class InvalidCreateSessionRequestException extends InvalidException {
    public InvalidCreateSessionRequestException(
            Collection<String> errors
    ) {
        super("The request to create an access token contained invalid data.", errors);
    }
}
