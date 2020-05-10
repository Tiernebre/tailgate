package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.session.CreateSessionRequest;

import java.util.UUID;

public final class TokenFactory {
    public static CreateSessionRequest generateOneCreateRequest() {
        return CreateSessionRequest.builder()
                .email(UUID.randomUUID().toString() + ".com")
                .password(UUID.randomUUID().toString())
                .build();
    }
}
