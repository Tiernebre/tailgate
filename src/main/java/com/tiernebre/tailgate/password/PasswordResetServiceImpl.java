package com.tiernebre.tailgate.password;

import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenService;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {
    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordResetTokenDeliveryService passwordResetTokenDeliveryService;

    @Override
    public void createOne(PasswordResetRequest passwordResetRequest) {
        userService
                .findOneByEmail(passwordResetRequest.getEmail())
                .ifPresent(this::sendOffPasswordResetTokenToUser);
    }

    private void sendOffPasswordResetTokenToUser(UserDto user) {
        String passwordResetToken = passwordResetTokenService.createOneForUser(user);
        passwordResetTokenDeliveryService.sendOne(user, passwordResetToken);
    }
}
