package com.tiernebre.tailgate.user;

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
}
