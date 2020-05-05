package com.tiernebre.tailgate.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundForTokenException extends Exception {
    public UserNotFoundForTokenException(String message) {
        super(message);
    }
}
