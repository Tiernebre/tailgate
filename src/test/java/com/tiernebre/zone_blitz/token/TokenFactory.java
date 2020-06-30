package com.tiernebre.zone_blitz.token;

import com.tiernebre.zone_blitz.session.CreateSessionRequest;

import java.util.UUID;

public final class TokenFactory {
    public static CreateSessionRequest generateOneCreateRequest() {
        return CreateSessionRequest.builder()
                .email(UUID.randomUUID().toString() + ".com")
                .password(UUID.randomUUID().toString())
                .build();
    }
}
