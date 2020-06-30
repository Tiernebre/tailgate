package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.user.dto.UserDto;

public interface PasswordResetTokenService {
    /**
     * Creates a password reset token for a provided user.
     *
     * @param user The user to create a password reset token for.
     * @return The created password reset token.
     */
    String createOneForUser(UserDto user);

    /**
     * Deletes a password reset token asynchronously.
     *
     * @param token The password reset token to delete.
     */
    void deleteOneAsynchronously(String token);
}
