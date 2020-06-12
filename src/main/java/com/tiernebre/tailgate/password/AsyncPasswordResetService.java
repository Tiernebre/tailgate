package com.tiernebre.tailgate.password;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Asynchronous wrapper around the normal primary implementation of the
 * password reset service.
 */
@Service
@Primary
@RequiredArgsConstructor
public class AsyncPasswordResetService implements PasswordResetService {
    private final PasswordResetService passwordResetService;

    @Override
    @Async
    public void createOne(PasswordResetRequest passwordResetRequest) {
        passwordResetService.createOne(passwordResetRequest);
    }
}
