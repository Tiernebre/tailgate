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
        boolean allAnswersAreValid = foundAnswers
                .entrySet()
                .stream()
                .allMatch(foundAnswer -> {
                    String providedAnswer = answersToValidate.get(foundAnswer.getKey());
                    return passwordEncoder.matches(providedAnswer, foundAnswer.getValue());
                });
        if (!allAnswersAreValid) {
            throw new InvalidSecurityQuestionAnswerException(Collections.singleton("Incorrect Security Question Answer"));
        }
    }
}
