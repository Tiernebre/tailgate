package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.exception.NotFoundException;

public class UserNotFoundForSessionException extends NotFoundException {
    public UserNotFoundForSessionException(String message) {
        super(message);
    }
}
