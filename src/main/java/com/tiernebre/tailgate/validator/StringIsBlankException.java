package com.tiernebre.tailgate.validator;

public class StringIsBlankException extends RuntimeException {
    public StringIsBlankException(String message) {
        super(message);
    }
}
