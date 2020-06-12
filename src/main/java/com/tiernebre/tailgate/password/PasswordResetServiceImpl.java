package com.tiernebre.tailgate.password;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    @Override
    public void createOne(PasswordResetRequest passwordResetRequest) {
    }
}
