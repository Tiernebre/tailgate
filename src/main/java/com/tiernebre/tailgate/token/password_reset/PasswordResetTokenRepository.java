package com.tiernebre.tailgate.token.password_reset;

import com.tiernebre.tailgate.user.dto.UserDto;

public interface PasswordResetTokenRepository {
    /**
     * Creates a password reset token for a provided user.
     *
     * @param user The user to create a password reset token for.
     * @return The created password reset token.
     */
    PasswordResetTokenEntity createOneForUser(UserDto user);

    /**
     * Deletes a password reset token.
     *
     * @param token The password reset token to delete.
     */
    void deleteOne(String token);
}
