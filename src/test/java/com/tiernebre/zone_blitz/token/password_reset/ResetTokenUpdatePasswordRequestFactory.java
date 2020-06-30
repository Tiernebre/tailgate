package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.user.dto.ResetTokenUpdatePasswordRequest;

import java.util.UUID;

public class ResetTokenUpdatePasswordRequestFactory {
    public static ResetTokenUpdatePasswordRequest generateOneDto() {
        String password = UUID.randomUUID().toString();
        return ResetTokenUpdatePasswordRequest.builder()
                .email(password)
                .newPassword(password)
                .confirmationNewPassword(UUID.randomUUID().toString())
                .build();
    }
}
