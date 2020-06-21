package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.exception.InvalidSecurityQuestionAnswerException;

import java.util.Map;

public interface UserSecurityQuestionsService {
    void validateAnswersForUserWithEmailAndResetToken(
            String email,
            String resetToken,
            Map<Long, String> answersToValidate
    ) throws InvalidSecurityQuestionAnswerException;
}
