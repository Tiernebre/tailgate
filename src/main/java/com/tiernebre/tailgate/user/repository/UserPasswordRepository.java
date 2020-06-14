package com.tiernebre.tailgate.user.repository;

/**
 * Manages passwords for a user.
 */
public interface UserPasswordRepository {
    /**
     * Updates a user's password using a given reset token and a user email.
     * @param password The password to update to.
     * @param email The email of the user to update
     * @param resetToken The password reset token also tied to a password reset request.
     */
    void updateOneWithEmailAndNonExpiredResetToken(String password, String email, String resetToken);
}
