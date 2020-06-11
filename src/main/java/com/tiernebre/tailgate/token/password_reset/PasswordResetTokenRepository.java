package com.tiernebre.tailgate.token.password_reset;

import com.tiernebre.tailgate.user.dto.UserDto;

public interface PasswordResetTokenRepository {
    /**
     * Creates a confirmation token for a provided user.
     *
     * @param user The user to create a confirmation token for.
     * @return The created confirmation token.
     */
    PasswordResetTokenEntity createOneForUser(UserDto user);

    /**
     * Deletes a confirmation token.
     *
     * @param token The confirmation token to delete.
     */
    void deleteOne(String token);
}
