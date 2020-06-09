package com.tiernebre.tailgate.token.access;

import com.tiernebre.tailgate.user.dto.UserDto;

public interface AccessTokenProvider {
    /**
     * Generates an access token given information about a user.
     * @param user The user to generate an authentication token for.
     * @return An access token used for authentication to allow a user to perform authenticated only actions.
     */
    String generateOne(UserDto user) throws GenerateAccessTokenException;

    /**
     * Validates if a given access token is legitimate.
     * @return The User representation of the access token payload if successful.
     */
    UserDto validateOne(String token);
}
