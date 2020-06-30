package com.tiernebre.zone_blitz.user.exception;

import com.tiernebre.zone_blitz.exception.NotFoundException;

public class UserNotFoundForPasswordUpdateException extends NotFoundException {
    public UserNotFoundForPasswordUpdateException() {
        super("No user password update occurred due to invalid information provided.");
    }
}
