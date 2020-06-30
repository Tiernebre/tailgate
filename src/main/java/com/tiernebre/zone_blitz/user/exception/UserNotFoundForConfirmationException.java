package com.tiernebre.zone_blitz.user.exception;

import com.tiernebre.zone_blitz.exception.NotFoundException;

public class UserNotFoundForConfirmationException extends NotFoundException {
    public UserNotFoundForConfirmationException() {
        super("The user to confirm was not found");
    }
}
