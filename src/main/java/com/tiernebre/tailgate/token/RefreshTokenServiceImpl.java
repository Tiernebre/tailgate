package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;

import java.util.Optional;

public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Override
    public String createOneForUser(UserDto user) {
        return null;
    }

    @Override
    public Optional<RefreshTokenDto> getOne(String token) {
        return Optional.empty();
    }
}
