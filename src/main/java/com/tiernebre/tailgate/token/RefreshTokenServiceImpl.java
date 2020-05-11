package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final RefreshTokenConverter converter;

    @Override
    public String createOneForUser(UserDto user) {
        return repository.createOneForUser(user).getToken();
    }

    @Override
    public Optional<RefreshTokenDto> findOneById(String id) {
        return repository.findOneById(id)
                .map(converter::convertToDto);
    }
}
