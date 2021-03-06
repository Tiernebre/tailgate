package com.tiernebre.zone_blitz.user.service;

import com.tiernebre.zone_blitz.user.exception.InvalidSecurityQuestionAnswerException;
import com.tiernebre.zone_blitz.user.repository.UserSecurityQuestionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSecurityQuestionServiceImpl implements UserSecurityQuestionService {
    private final UserSecurityQuestionRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void validateAnswersForUserWithEmailAndResetToken(
            String email,
            UUID resetToken,
            Map<Long, String> answersToValidate
    ) throws InvalidSecurityQuestionAnswerException {
        Map<Long, String> foundAnswers = repository.getAnswersForEmailAndResetToken(email, resetToken);
        boolean allAnswersAreValid = foundAnswers
                .entrySet()
                .stream()
                .allMatch(foundAnswer -> {
                    String providedAnswer = answersToValidate.get(foundAnswer.getKey());
                    return StringUtils.isNotBlank(providedAnswer) && passwordEncoder.matches(providedAnswer, foundAnswer.getValue());
                });
        if (!allAnswersAreValid) {
            throw new InvalidSecurityQuestionAnswerException(Collections.singleton("Incorrect Security Question Answer"));
        }
    }
}
