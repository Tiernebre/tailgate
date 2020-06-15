package com.tiernebre.tailgate.user.exception;

import com.tiernebre.tailgate.exception.InvalidException;

import java.util.Set;

public class InvalidUpdatePasswordRequestException extends InvalidException {
    public InvalidUpdatePasswordRequestException(
        Set<String> errors
    ) {
        super("The request to update a user password was invalid.", errors);
    }
}
