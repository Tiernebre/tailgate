package com.tiernebre.tailgate.user.exception;

import com.tiernebre.tailgate.exception.NotFoundException;

public class UserNotFoundForPasswordUpdateException extends NotFoundException {
    public UserNotFoundForPasswordUpdateException() {
        super("No user password update occurred due to invalid information provided.");
    }
}
