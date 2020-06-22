package com.tiernebre.tailgate.authentication;

import com.tiernebre.tailgate.user.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticatedUserService {
    public static UserDto getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserDto) authentication.getPrincipal();
    }
}
