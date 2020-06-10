package com.tiernebre.tailgate.validator;

import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class BaseValidator {
    private final Validator validator;

    protected <T> Set<String> validateCommon(T valueToValidate) {
        return validator
                .validate(valueToValidate)
                .stream()
                .map(this::formatErrorMessage)
                .collect(Collectors.toSet());
    }

    private <T> String formatErrorMessage(ConstraintViolation<T> constraintViolation) {
        String originalMessage = constraintViolation.getMessage();
        boolean customMessageIsProvided = Objects.equals(originalMessage, constraintViolation.getMessageTemplate());
        return customMessageIsProvided ? originalMessage : String.format("%s %s", constraintViolation.getPropertyPath(), originalMessage);
    }
}
