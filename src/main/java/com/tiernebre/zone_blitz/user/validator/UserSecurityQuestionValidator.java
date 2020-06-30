package com.tiernebre.zone_blitz.user.validator;

import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;

import java.util.Set;

/**
 * Validates submitted user security questions to ensure it meets requirements.
 */
public interface UserSecurityQuestionValidator {
    /**
     * Validates the security questions chosen along with their answers for a user.
     *
     * @param createUserRequest The request to validate
     * @return The error messages found when validating submitted security questions.
     */
    Set<String> validate(CreateUserRequest createUserRequest);
}
