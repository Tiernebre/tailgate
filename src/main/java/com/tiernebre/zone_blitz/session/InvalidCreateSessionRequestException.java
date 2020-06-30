package com.tiernebre.zone_blitz.session;

import com.tiernebre.zone_blitz.exception.InvalidException;

import java.util.Set;

public class InvalidCreateSessionRequestException extends InvalidException {
    public InvalidCreateSessionRequestException(
            Set<String> errors
    ) {
        super("The request to create an access token contained invalid data.", errors);
    }
}
