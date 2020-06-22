package com.tiernebre.tailgate.authentication;

import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.assertEquals;

public class AuthenticatedUserServiceTests {
    @BeforeEach
    public void setup() {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.createEmptyContext();
    }

    @Nested
    @DisplayName("getCurrentAuthenticatedUser")
    public class GetCurrentAuthenticatedUserTests {
        @Test
        @DisplayName("returns the currently authenticated user")
        void returnsTheCurrentlyAuthenticatedUser() {
            UserDto expectedUser = UserFactory.generateOneDto();
            Authentication authentication = new UsernamePasswordAuthenticationToken(expectedUser, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDto currentAuthenticatedUser = AuthenticatedUserService.getCurrentAuthenticatedUser();
            assertEquals(expectedUser, currentAuthenticatedUser);
        }
    }
}
