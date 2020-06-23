package com.tiernebre.tailgate.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserUpdatePasswordRequest {
    String oldPassword;
    String newPassword;
    String confirmationNewPassword;
}
