package com.tiernebre.zone_blitz.authentication;

import com.tiernebre.zone_blitz.token.access.AccessTokenProvider;
import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.tiernebre.zone_blitz.authentication.SessionCookieNames.FINGERPRINT_COOKIE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthorizationFilterTests {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AccessTokenProvider tokenProvider;

    @InjectMocks
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Nested
    @DisplayName("doFilterInternal")
    public class DoFilterInternalTests {
        @ParameterizedTest(name = "does set an authentication in the context if token = \"{0}\"")
        @ValueSource(strings = {
                "",
                " ",
                "test",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiZWNydXRlYWsiLCJlbWFpbCI6InRpZXJuZWJyZUBnbWFpbC5jb20ifQ.QCe0mYNZXYyDFF7vYlkGclYBLV-ml0kCQdBDi5wFDo0"
        })
        @NullSource
        void testNotSetAuthenticationSituationsForTokenBeing(String token) throws IOException, ServletException {
            assertSecurityContextHolderDoesNotGetSetForToken(token);
        }

        @Test
        @DisplayName("sets authentication in the context for a valid token")
        void setsAuthenticationForValidToken() throws IOException, ServletException {
            HttpServletRequest req = mock(HttpServletRequest.class);
            HttpServletResponse res = mock(HttpServletResponse.class);
            FilterChain filterChain = mock(FilterChain.class);
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiZWNydXRlYWsiLCJlbWFpbCI6InRpZXJuZWJyZUBnbWFpbC5jb20ifQ.QCe0mYNZXYyDFF7vYlkGclYBLV-ml0kCQdBDi5wFDo0";
            when(req.getHeader(eq("Authorization"))).thenReturn("Bearer " + token);
            String fingerprint = UUID.randomUUID().toString();
            Cookie[] cookies = { new Cookie(FINGERPRINT_COOKIE_NAME, fingerprint) };
            when(req.getCookies()).thenReturn(cookies);
            UserDto authorizedUser = UserFactory.generateOneDto();
            when(tokenProvider.validateOne(eq(token), eq(fingerprint))).thenReturn(authorizedUser);
            jwtAuthorizationFilter.doFilterInternal(req, res, filterChain);
            assertEquals(authorizedUser, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }

        private void assertSecurityContextHolderDoesNotGetSetForToken(String token) throws IOException, ServletException {
            HttpServletRequest req = mock(HttpServletRequest.class);
            HttpServletResponse res = mock(HttpServletResponse.class);
            FilterChain filterChain = mock(FilterChain.class);
            when(req.getHeader(eq("Authorization"))).thenReturn(token);
            jwtAuthorizationFilter.doFilterInternal(req, res, filterChain);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }
    }
}
