package com.tiernebre.tailgate.token.refresh;

import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.validator.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.tiernebre.tailgate.token.refresh.RefreshTokenConstants.BLANK_TOKEN_ERROR_MESSAGE;
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
        StringValidator.requireNonBlank(token, BLANK_TOKEN_ERROR_MESSAGE);

        repository.deleteOne(token);
    }
}
