package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.repository.UserSecurityQuestionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserSecurityQuestionsServiceImpl implements UserSecurityQuestionsService {
    private final UserSecurityQuestionsRepository repository;

    @Override
    public void validateAnswersForUserWithEmailAndResetToken(
            String email,
            String resetToken,
            Map<Long, String> answersToValidate
    ) {
    }
}
