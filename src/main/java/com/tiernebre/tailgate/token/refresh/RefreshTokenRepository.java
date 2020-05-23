package com.tiernebre.tailgate.token.refresh;

import com.tiernebre.tailgate.user.UserDto;

public interface RefreshTokenRepository {
    /**
     * Saves a refresh token for a given user. The generation of the refresh token is handled in this method.
     * @param user The user to save a refresh token for.
     * @return The saved refresh token.
     */
    RefreshTokenEntity createOneForUser(UserDto user);

    /**
     * Deletes a Refresh Token by its given unique token value.
     * @param token The token of the refresh token to delete.
     */
    void deleteOne(String token);
}
