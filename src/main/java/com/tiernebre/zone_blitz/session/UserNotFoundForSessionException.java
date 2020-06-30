package com.tiernebre.zone_blitz.session;

import com.tiernebre.zone_blitz.exception.NotFoundException;

public class UserNotFoundForSessionException extends NotFoundException {
    public UserNotFoundForSessionException(String message) {
        super(message);
    }
}
