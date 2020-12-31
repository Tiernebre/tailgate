package com.tiernebre.zone_blitz.token.user_confirmation;

import com.tiernebre.zone_blitz.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserConfirmationTokenServiceImpl implements UserConfirmationTokenService {
    private final UserConfirmationTokenRepository repository;

    @Override
    public UUID findOrGenerateForUser(UserDto user) {
        return repository
                .findOneForUser(user)
                .map(UserConfirmationTokenEntity::getToken)
                .orElseGet(() -> createOneForUser(user));
    }

    @Override
    public UUID createOneForUser(UserDto user) {
        return repository.createOneForUser(user).getToken();
    }
}
