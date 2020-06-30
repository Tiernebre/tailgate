package com.tiernebre.zone_blitz.validator;

/**
 * Allows for validation of data structures.
 * @param <T> The specific type to validate.
 */
public interface Validator<T, U extends Exception> {
    /**
     * Validates a given value.
     * @param valueToValidate The value to validate
     * @throws U If anything about the value was invalid.
     */
    void validate(T valueToValidate) throws U;
}
