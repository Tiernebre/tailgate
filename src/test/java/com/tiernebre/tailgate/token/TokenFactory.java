package com.tiernebre.tailgate.token;

import java.util.UUID;

public final class TokenFactory {
    public static CreateAccessTokenRequest generateOneCreateRequest() {
        return CreateAccessTokenRequest.builder()
                .email(UUID.randomUUID().toString() + ".com")
                .password(UUID.randomUUID().toString())
                .build();
    }
}
