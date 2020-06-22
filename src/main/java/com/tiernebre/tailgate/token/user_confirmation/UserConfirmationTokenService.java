package com.tiernebre.tailgate.token.user_confirmation;

import com.tiernebre.tailgate.user.dto.UserDto;

public interface UserConfirmationTokenService {
    /**
     * Creates a confirmation token for a provided user.
     *
     * @param user The user to create a confirmation token for.
     * @return The created confirmation token.
     */
    String createOneForUser(UserDto user);

    /**
     * Tries to find a confirmation token for a user. If for some
     * reason a confirmation token does not exist for a user, it
     * generates a brand new one and uses that instead.
     *
     * @param user The user to either find or create a new confirmation token for.
     * @return The found or generated confirmation token.
     */
    String findOrGenerateForUser(UserDto user);
}
