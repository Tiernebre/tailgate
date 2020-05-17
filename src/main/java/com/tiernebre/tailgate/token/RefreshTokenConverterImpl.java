package com.tiernebre.tailgate.token;

import org.springframework.stereotype.Component;

@Component
public class RefreshTokenConverterImpl implements RefreshTokenConverter {
    @Override
    public RefreshTokenDto convertToDto(RefreshTokenEntity refreshTokenEntity) {
        return RefreshTokenDto.builder()
                .token(refreshTokenEntity.getToken())
                .userId(refreshTokenEntity.getUserId())
                .build();
    }
}