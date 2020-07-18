package com.tiernebre.zone_blitz.token.access;

import java.util.UUID;

public class AccessTokenFactory {
    public static AccessTokenDto generateOneDto() {
        return AccessTokenDto.builder()
                .token(UUID.randomUUID().toString())
                .fingerprint(UUID.randomUUID().toString())
                .build();
    }
}
