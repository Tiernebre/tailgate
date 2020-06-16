package com.tiernebre.tailgate.user.repository;

import java.util.Map;

/**
 * Manages passwords for a user.
 */
public interface UserPasswordRepository {
    /**
     * Updates a user's password using a given reset token and a user email.
     * @param password The password to update to.
     * @param email The email of the user to update
     * @param resetToken The password reset token also tied to a password reset request.
     * @return true if the user was found and updated, false if no user was found and updated with the criteria provided.
     */
    boolean updateOneWithEmailAndNonExpiredResetToken(String password, String email, String resetToken);

    /**
     * Returns the security question answers tied to a given email and non expired reset password token.
     * @param email The email of the user who requested to reset the password.
     * @param resetToken The reset token assigned to that user's password reset request.
     * @return a map that uses a security question ID as a key, with the value being the answer.
     */
    Map<Long, String> getSecurityQuestionAnswersForEmailAndNonExpiredResetToken(String email, String resetToken);
}
