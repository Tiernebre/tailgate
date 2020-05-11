package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;

import java.util.Optional;

public interface RefreshTokenService {
    /**
     * Creates a refresh token that is stored and connected to a User to allow for easy future refreshes of access tokens.
     * @param user The user to create a refresh token for.
     * @return The refresh token that a client can use to refresh tokens for a user.
     */
    String createOneForUser(UserDto user);

    /**
     * Retrieves a Refresh Token that contains information about the user who it's connected to.
     * @param token The token to look-up.
     * @return An optional containing the refresh token. Empty if it was not found, or was expired.
     */
    Optional<RefreshTokenDto> getOneById(String token);
}
