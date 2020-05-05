package com.tiernebre.tailgate.token;

import java.util.UUID;

public final class TokenFactory {
    public static CreateTokenRequest generateOneCreateRequest() {
        return CreateTokenRequest.builder()
                .email(UUID.randomUUID().toString() + ".com")
                .password(UUID.randomUUID().toString())
                .build();
    }
}
