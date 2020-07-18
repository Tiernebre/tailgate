package com.tiernebre.zone_blitz.token.access;

import com.tiernebre.zone_blitz.exception.InvalidException;

import java.util.Collections;

public class AccessTokenInvalidException extends InvalidException {
    private static final String errorMessage = "The access token provided for authentication was not valid.";

    public AccessTokenInvalidException() {
        super(errorMessage, Collections.singleton(errorMessage));
    }
}
