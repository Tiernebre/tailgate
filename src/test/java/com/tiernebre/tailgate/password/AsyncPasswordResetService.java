package com.tiernebre.tailgate.password;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncPasswordResetService implements PasswordResetService {
    private PasswordResetService passwordResetService;

    @Override
    public void createOne(PasswordResetRequest passwordResetRequest) {
        passwordResetService.createOne(passwordResetRequest);
    }
}
