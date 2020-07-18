package com.tiernebre.zone_blitz.password;

import com.tiernebre.zone_blitz.token.password_reset.PasswordResetTokenService;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        UUID passwordResetToken = passwordResetTokenService.createOneForUser(user);
        passwordResetTokenDeliveryService.sendOne(user, passwordResetToken);
    }
}
