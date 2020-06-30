package com.tiernebre.zone_blitz.user.service;

import com.tiernebre.zone_blitz.user.exception.InvalidSecurityQuestionAnswerException;

import java.util.Map;

public interface UserSecurityQuestionsService {
    void validateAnswersForUserWithEmailAndResetToken(
            String email,
            String resetToken,
            Map<Long, String> answersToValidate
    ) throws InvalidSecurityQuestionAnswerException;
}
