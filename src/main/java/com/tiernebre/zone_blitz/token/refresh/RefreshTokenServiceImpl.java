package com.tiernebre.zone_blitz.token.refresh;

import com.tiernebre.zone_blitz.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static com.tiernebre.zone_blitz.token.refresh.RefreshTokenConstants.NULL_USER_ERROR_MESSAGE;

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
    public void deleteOne(UUID token) {
        repository.deleteOne(token);
    }
}
