package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;

public interface RefreshTokenService {
    /**
     * Creates a refresh token that is stored and connected to a User to allow for easy future refreshes of access tokens.
     * @param user The user to create a refresh token for.
     * @return The refresh token that a client can use to refresh tokens for a user.
     */
    String createOneForUser(UserDto user);
}
