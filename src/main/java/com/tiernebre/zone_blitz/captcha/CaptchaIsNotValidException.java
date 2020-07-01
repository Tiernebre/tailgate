package com.tiernebre.zone_blitz.captcha;

import com.tiernebre.zone_blitz.exception.InvalidException;

import java.util.Collections;

public class CaptchaIsNotValidException extends InvalidException {
    private final static String ERROR_MESSAGE = "The Captcha token provided was deemed to be invalid.";

    protected CaptchaIsNotValidException() {
        super(ERROR_MESSAGE, Collections.singleton(ERROR_MESSAGE));
    }
}
