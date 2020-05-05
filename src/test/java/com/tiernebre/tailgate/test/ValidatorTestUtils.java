package com.tiernebre.tailgate.test;

import com.tiernebre.tailgate.exception.InvalidException;
import com.tiernebre.tailgate.validator.Validator;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Provides useful utilities for asserting validation tests
 */
public final class ValidatorTestUtils {
    public static <T, U extends InvalidException> void assertThatValidationInvalidatedCorrectly(
            Validator<T, U> validator,
            T value,
            Class<U> exception,
            String errorMessageToCheck
    ) {
        Collection<String> errors = assertThrows(exception, () -> {
            validator.validate(value);
        }).getErrors();
        assertTrue(errors.contains(errorMessageToCheck));
    }
}
