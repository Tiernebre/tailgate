package com.tiernebre.tailgate.user.service;

import java.util.Map;

public interface UserSecurityQuestionsService {
    void validateAnswersForUserWithEmailAndResetToken(
            String email,
            String resetToken,
            Map<Long, String> answersToValidate
    );
}
