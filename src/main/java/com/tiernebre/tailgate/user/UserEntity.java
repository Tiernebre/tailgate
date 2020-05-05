package com.tiernebre.tailgate.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserEntity {
    Long id;
    String email;
    String password;
}
