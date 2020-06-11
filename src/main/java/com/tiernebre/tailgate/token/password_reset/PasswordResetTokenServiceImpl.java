package com.tiernebre.tailgate.token.password_reset;

import com.tiernebre.tailgate.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final PasswordResetTokenRepository repository;

    @Override
    public String createOneForUser(UserDto user) {
        return repository.createOneForUser(user).getToken();
    }

    @Override
    public void deleteOne(String token) {
        repository.deleteOne(token);
    }
}
