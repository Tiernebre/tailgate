package com.tiernebre.tailgate.password;

import java.util.UUID;

public class PasswordResetFactory {
    public static PasswordResetRequest generateOneRequest() {
        return PasswordResetRequest.builder()
                .email(UUID.randomUUID().toString() + "@test.com")
                .build();
    }
}
