package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDTO;

public interface TokenProvider {
    /**
     * Generates an authentication token given information about a user.
     * @return A token used for authentication
     */
    String generateOne(UserDTO user) throws GenerateTokenException;

    /**
     * Validates if a given token is legitimate.
     * @return The User representation of the token payload if successful.
     */
    UserDTO validateOne(String token);
}
