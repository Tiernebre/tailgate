package com.tiernebre.zone_blitz.token.refresh;

import com.tiernebre.zone_blitz.user.dto.UserDto;

import java.util.UUID;

public interface RefreshTokenService {
    /**
     * Creates a refresh token that is stored and connected to a User to allow for easy future refreshes of access tokens.
     * @param user The user to create a refresh token for.
     * @return The refresh token that a client can use to refresh tokens for a user.
     */
    UUID createOneForUser(UserDto user);

    /**
     * Deletes a Refresh Token by its given unique token value.
     * @param token The token of the refresh token to delete.
     */
    void deleteOne(UUID token);
}
