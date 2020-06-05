package com.tiernebre.tailgate.user;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncUserEmailConfirmationService implements UserConfirmationService {
    private final UserEmailConfirmationService userEmailConfirmationService;

    @Override
    @Async
    public void sendOne(UserDto userToConfirm) {
        userEmailConfirmationService.sendOne(userToConfirm);
    }
}
