package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;

public interface RefreshTokenRepository {
    /**
     * Saves a refresh token for a given user. The generation of the refresh token is handled in this method.
     * @param user The user to save a refresh token for.
     * @return The saved refresh token.
     */
    RefreshTokenEntity createOneForUser(UserDto user);
}
