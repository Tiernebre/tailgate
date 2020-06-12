package com.tiernebre.tailgate.forgot_password_request;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ForgetPasswordRequest {
    String email;
}
