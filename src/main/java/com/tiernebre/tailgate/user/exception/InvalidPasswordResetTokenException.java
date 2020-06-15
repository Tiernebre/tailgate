package com.tiernebre.tailgate.user.exception;

import com.tiernebre.tailgate.exception.InvalidException;

import java.util.Set;

public class InvalidPasswordResetTokenException extends InvalidException {
    public InvalidPasswordResetTokenException(Set<String> errors) {
        super("The password reset token provided was invalid.", errors);
    }
}
