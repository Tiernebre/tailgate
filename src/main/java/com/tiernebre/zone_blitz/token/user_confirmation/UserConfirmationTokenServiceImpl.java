package com.tiernebre.zone_blitz.token.user_confirmation;

import com.tiernebre.zone_blitz.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConfirmationTokenServiceImpl implements UserConfirmationTokenService {
    private final UserConfirmationTokenRepository repository;

    @Override
    public String findOrGenerateForUser(UserDto user) {
        return repository
                .findOneForUser(user)
                .map(UserConfirmationTokenEntity::getToken)
                .orElseGet(() -> createOneForUser(user));
    }

    @Override
    public String createOneForUser(UserDto user) {
        return repository.createOneForUser(user).getToken();
    }
}
