package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.exception.InvalidSecurityQuestionAnswerException;
import com.tiernebre.tailgate.user.repository.UserSecurityQuestionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserSecurityQuestionsServiceImpl implements UserSecurityQuestionsService {
    private final UserSecurityQuestionsRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void validateAnswersForUserWithEmailAndResetToken(
            String email,
            String resetToken,
            Map<Long, String> answersToValidate
    ) throws InvalidSecurityQuestionAnswerException {
        Map<Long, String> foundAnswers = repository.getAnswersForEmailAndResetToken(email, resetToken);
        boolean allAnswersAreValid = answersToValidate
                .entrySet()
                .stream()
                .allMatch(providedAnswer -> {
                    String answerToValidateAgainst = foundAnswers.get(providedAnswer.getKey());
                    return passwordEncoder.matches(providedAnswer.getValue(), answerToValidateAgainst);
                });
        if (!allAnswersAreValid) {
            throw new InvalidSecurityQuestionAnswerException(Collections.singleton("Incorrect Security Question Answer"));
        }
    }
}
