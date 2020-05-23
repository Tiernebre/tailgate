package com.tiernebre.tailgate.token.refresh;

import com.tiernebre.tailgate.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.tiernebre.tailgate.token.refresh.RefreshTokenConstants.NULL_USER_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository repository;

    @Override
    public String createOneForUser(UserDto user) {
        Objects.requireNonNull(user, NULL_USER_ERROR_MESSAGE);

        return repository.createOneForUser(user).getToken();
    }

    @Override
    public void deleteOne(String token) {
        repository.deleteOne(token);
    }
}
