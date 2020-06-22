package com.tiernebre.tailgate.test;

import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.dto.UserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDto principal = UserFactory.CUSTOM_AUTHENTICATED_USER;
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, new ArrayList<>());
        context.setAuthentication(auth);
        return context;
    }
}
