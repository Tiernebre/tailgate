package com.tiernebre.tailgate.user.exception;

import com.tiernebre.tailgate.exception.InvalidException;

import java.util.Set;

public class InvalidSecurityQuestionAnswerException extends InvalidException {
    public InvalidSecurityQuestionAnswerException(Set<String> errors) {
        super("The provided security question answers are not valid.", errors);
    }
}
