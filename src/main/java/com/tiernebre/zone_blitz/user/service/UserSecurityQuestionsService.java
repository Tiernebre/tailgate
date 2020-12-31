package com.tiernebre.zone_blitz.user.service;

import com.tiernebre.zone_blitz.user.exception.InvalidSecurityQuestionAnswerException;

import java.util.Map;
import java.util.UUID;

public interface UserSecurityQuestionsService {
    void validateAnswersForUserWithEmailAndResetToken(
            String email,
            UUID resetToken,
            Map<Long, String> answersToValidate
    ) throws InvalidSecurityQuestionAnswerException;
}
