package com.tiernebre.zone_blitz.token.access;

import com.tiernebre.zone_blitz.exception.UnauthorizedException;

public class AccessTokenInvalidException extends UnauthorizedException {
    private static final String errorMessage = "The access token provided for authentication was not valid.";

    public AccessTokenInvalidException() {
        super(errorMessage);
    }
}
