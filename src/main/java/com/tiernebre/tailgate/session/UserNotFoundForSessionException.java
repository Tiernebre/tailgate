package com.tiernebre.tailgate.session;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundForSessionException extends Exception {
    public UserNotFoundForSessionException(String message) {
        super(message);
    }
}
