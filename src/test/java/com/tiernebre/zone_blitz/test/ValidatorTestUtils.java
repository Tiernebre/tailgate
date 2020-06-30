package com.tiernebre.zone_blitz.test;

import com.tiernebre.zone_blitz.exception.InvalidException;
import com.tiernebre.zone_blitz.validator.Validator;

import java.util.Set;

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
        Set<String> errors = assertThrows(exception, () -> {
            validator.validate(value);
        }).getErrors();
        assertTrue(errors.contains(errorMessageToCheck));
    }
}
