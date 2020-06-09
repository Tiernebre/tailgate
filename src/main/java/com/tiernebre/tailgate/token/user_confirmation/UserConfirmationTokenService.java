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
     * Deletes a confirmation token.
     *
     * @param token The confirmation token to delete.
     */
    void deleteOne(String token);
}
