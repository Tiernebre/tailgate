package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;

import java.time.Clock;

public interface TokenProvider {
    /**
     * Generates an authentication token given information about a user.
     * @param user The user to generate an authentication token for.
     * @param clock A clock to use for time-based attributes of the authentication token generation.
     * @return A token used for authentication.
     */
    String generateOne(UserDto user, Clock clock) throws GenerateTokenException;

    /**
     * Validates if a given token is legitimate.
     * @return The User representation of the token payload if successful.
     */
    UserDto validateOne(String token);
}
