package com.tiernebre.tailgate.password;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncPasswordResetService implements PasswordResetService {
    private final PasswordResetService passwordResetService;

    @Async
    @Override
    public void createOne(PasswordResetRequest passwordResetRequest) {
        passwordResetService.createOne(passwordResetRequest);
    }
}
