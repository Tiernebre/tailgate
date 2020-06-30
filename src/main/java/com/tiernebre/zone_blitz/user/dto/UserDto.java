package com.tiernebre.zone_blitz.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@Value
@EqualsAndHashCode(callSuper = true)
@Builder
public class UserDto extends RepresentationModel<UserDto> {
    Long id;
    String email;
    @JsonIgnore
    boolean isConfirmed;
}
