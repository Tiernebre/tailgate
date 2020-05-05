package com.tiernebre.tailgate.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.RepresentationModel;

@Value
@EqualsAndHashCode(callSuper = true)
@Builder
public class UserDTO extends RepresentationModel<UserDTO> {
    Long id;
    String email;
}
