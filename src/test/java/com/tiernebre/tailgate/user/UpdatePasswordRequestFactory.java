package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.user.dto.UserUpdatePasswordRequest;

import java.util.UUID;

public class UpdatePasswordRequestFactory {
    public static UserUpdatePasswordRequest generateOne() {
        String newPassword = UUID.randomUUID().toString();
        return UserUpdatePasswordRequest.builder()
                .oldPassword(UUID.randomUUID().toString())
                .newPassword(newPassword)
                .confirmationNewPassword(newPassword)
                .build();
    }
}
