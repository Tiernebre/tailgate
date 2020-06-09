package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class AsyncUserEmailConfirmationService implements UserConfirmationService {
    private final UserEmailConfirmationService userEmailConfirmationService;

    @Override
    @Async
    public void sendOne(UserDto userToConfirm) {
        userEmailConfirmationService.sendOne(userToConfirm);
    }
}
