package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;

import java.util.Set;

/**
 * Validates a password to ensure it meets the strength requirements and is more likely
 * to be a secure password.
 */
public interface UserPasswordValidator {
    /**
     * Validates a password meets strength requirements, and that the confirmation password matches it.
     *
     * @param password The main password to test the strength of.
     * @param confirmationPassword Confirmation password to check and see if it equals the main password.
     * @return The error messages found when validating the password.
     */
    Set<String> validate(String password, String confirmationPassword);

    /**
     * Validates a password update request.
     *
     * @param updatePasswordRequest The request to validate
     * @return The error messages found when validating the password update request.
     */
    Set<String> validateUpdateRequest(ResetTokenUpdatePasswordRequest updatePasswordRequest);
}
