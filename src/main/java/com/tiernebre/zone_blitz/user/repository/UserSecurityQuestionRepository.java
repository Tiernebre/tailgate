package com.tiernebre.zone_blitz.user.repository;

import java.util.Map;
import java.util.UUID;

public interface UserSecurityQuestionRepository {
    /**
     * Returns the security question answers tied to a given email and non expired reset password token.
     * @param email The email of the user who requested to reset the password.
     * @param resetToken The reset token assigned to that user's password reset request.
     * @return a map that uses a security question ID as a key, with the value being the answer.
     */
    Map<Long, String> getAnswersForEmailAndResetToken(String email, UUID resetToken);
}
