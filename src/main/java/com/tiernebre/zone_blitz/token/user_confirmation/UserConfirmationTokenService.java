package com.tiernebre.zone_blitz.token.user_confirmation;

import com.tiernebre.zone_blitz.user.dto.UserDto;

import java.util.UUID;

public interface UserConfirmationTokenService {
    /**
     * Creates a confirmation token for a provided user.
     *
     * @param user The user to create a confirmation token for.
     * @return The created confirmation token.
     */
    UUID createOneForUser(UserDto user);

    /**
     * Tries to find a confirmation token for a user. If for some
     * reason a confirmation token does not exist for a user, it
     * generates a brand new one and uses that instead.
     *
     * @param user The user to either find or create a new confirmation token for.
     * @return The found or generated confirmation token.
     */
    UUID findOrGenerateForUser(UserDto user);
}
