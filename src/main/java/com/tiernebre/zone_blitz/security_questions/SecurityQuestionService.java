package com.tiernebre.zone_blitz.security_questions;

import java.util.List;
import java.util.Set;

public interface SecurityQuestionService {
    /**
     * Returns all of the security questions
     * @return all of the security questions
     */
    List<SecurityQuestionDto> getAll();

    /**
     * Determines if a set with some ids do not exist.
     * @param ids The ids to check
     * @return false if _all_ ids match up to security questions
     *         true if at least one of the ids does not match up to a security question
     */
    boolean someDoNotExistWithIds(Set<Long> ids);

    /**
     * Returns the security questions tied to a password reset token.
     *
     * Useful for providing a list of questions for a user to answer in order to
     * reset their password.
     *
     * @param passwordResetToken The password reset token to find questions for.
     * @return The found security questions.
     */
    List<SecurityQuestionDto> getAllForPasswordResetToken(String passwordResetToken);
}
