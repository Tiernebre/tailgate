package com.tiernebre.tailgate.user.exception;

import com.tiernebre.tailgate.exception.NotFoundException;

public class UserNotFoundForConfirmationException extends NotFoundException {
    public UserNotFoundForConfirmationException() {
        super("The user to confirm was not found");
    }
}
