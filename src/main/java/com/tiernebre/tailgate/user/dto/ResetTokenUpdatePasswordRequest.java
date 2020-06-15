package com.tiernebre.tailgate.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ResetTokenUpdatePasswordRequest {
    String email;
    String newPassword;
    String confirmationNewPassword;
}
