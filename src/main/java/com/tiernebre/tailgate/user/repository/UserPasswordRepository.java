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
     * @return true if the user was found and updated, false if no user was found with the criteria provided.
     */
    boolean updateOneWithEmailAndNonExpiredResetToken(String password, String email, String resetToken);
}
