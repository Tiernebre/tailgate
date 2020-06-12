package com.tiernebre.tailgate.password;

import com.tiernebre.tailgate.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class AsyncPasswordResetTokenDeliveryService implements PasswordResetTokenDeliveryService {
    private final PasswordResetTokenDeliveryService passwordResetTokenDeliveryService;

    @Async
    @Override
    public void sendOne(UserDto userDto, String passwordResetToken) {
        passwordResetTokenDeliveryService.sendOne(userDto, passwordResetToken);
    }
}
