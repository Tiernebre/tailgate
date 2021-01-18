package com.tiernebre.zone_blitz.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {
    Long id;
    String email;
    @JsonIgnore
    boolean isConfirmed;
}
