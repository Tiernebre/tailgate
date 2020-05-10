package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;

import java.time.Clock;

public interface TokenProvider {
    /**
     * Generates an authentication token given information about a user.
     * @param user The user to generate an authentication token for.
     * @return A token used for authentication.
     */
    String generateOne(UserDto user) throws GenerateTokenException;

    /**
     * Validates if a given token is legitimate.
     * @return The User representation of the token payload if successful.
     */
    UserDto validateOne(String token);
}
