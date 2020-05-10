package com.tiernebre.tailgate.token;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundForAccessTokenException extends Exception {
    public UserNotFoundForAccessTokenException(String message) {
        super(message);
    }
}
