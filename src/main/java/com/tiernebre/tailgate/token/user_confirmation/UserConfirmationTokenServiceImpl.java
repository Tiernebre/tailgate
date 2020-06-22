package com.tiernebre.tailgate.token.user_confirmation;

import com.tiernebre.tailgate.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConfirmationTokenServiceImpl implements UserConfirmationTokenService {
    private final UserConfirmationTokenRepository repository;

    @Override
    public String createOneForUser(UserDto user) {
        return repository.createOneForUser(user).getToken();
    }
}
