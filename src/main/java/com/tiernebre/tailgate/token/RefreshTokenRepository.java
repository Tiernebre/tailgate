package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;

import java.util.Optional;

public interface RefreshTokenRepository {
    /**
     * Saves a refresh token for a given user. The generation of the refresh token is handled in this method.
     * @param user The user to save a refresh token for.
     * @return The saved refresh token.
     */
    RefreshTokenEntity createOneForUser(UserDto user);

    /**
     * Finds a refresh token by its ID, which is the actual string value of the token.
     * @param id The id of the refresh token, which is just the token string.
     * @return The found refresh token.
     */
    Optional<RefreshTokenEntity> findOneById(String id);
}
