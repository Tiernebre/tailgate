package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.user.dto.UpdatePasswordRequest;

import java.util.UUID;

public class UpdatePasswordRequestFactory {
    public static UpdatePasswordRequest generateOne() {
        String newPassword = UUID.randomUUID().toString();
        return UpdatePasswordRequest.builder()
                .oldPassword(UUID.randomUUID().toString())
                .newPassword(newPassword)
                .confirmationNewPassword(newPassword)
                .build();
    }
}
