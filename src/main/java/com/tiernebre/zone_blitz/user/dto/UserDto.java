package com.tiernebre.zone_blitz.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class UserDto {
    Long id;
    String email;
    @JsonIgnore
    @Getter(onMethod = @__(@JsonIgnore))
    boolean isConfirmed;
}
