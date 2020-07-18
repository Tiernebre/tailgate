package com.tiernebre.zone_blitz.authentication;

import com.tiernebre.zone_blitz.token.access.AccessTokenInvalidException;
import com.tiernebre.zone_blitz.token.access.AccessTokenProvider;
import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.UUID;

import static com.tiernebre.zone_blitz.authentication.SessionCookieNames.FINGERPRINT_COOKIE_NAME;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtAuthorizationFilterTests {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AccessTokenProvider tokenProvider;

    @InjectMocks
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
        SecurityContextHolder.createEmptyContext();
    }

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
        void testNotSetAuthenticationSituationsForTokenBeing(String token) throws IOException, ServletException {
            assertSecurityContextHolderDoesNotGetSetForToken(token);
        }

        @Test
        @DisplayName("sets authentication in the context for a valid token")
        void setsAuthenticationForValidToken() throws IOException, ServletException, AccessTokenInvalidException {
            MockHttpServletRequest req = new MockHttpServletRequest();
            MockHttpServletResponse res = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiZWNydXRlYWsiLCJlbWFpbCI6InRpZXJuZWJyZUBnbWFpbC5jb20ifQ.QCe0mYNZXYyDFF7vYlkGclYBLV-ml0kCQdBDi5wFDo0";
            req.addHeader("Authorization", "Bearer " + token);
            String fingerprint = UUID.randomUUID().toString();
            Cookie[] cookies = { new Cookie(FINGERPRINT_COOKIE_NAME, fingerprint) };
            req.setCookies(cookies);
            UserDto authorizedUser = UserFactory.generateOneDto();
            when(tokenProvider.validateOne(eq(token), eq(fingerprint))).thenReturn(authorizedUser);
            jwtAuthorizationFilter.doFilterInternal(req, res, filterChain);
            assertEquals(authorizedUser, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }

        @Test
        @DisplayName("sets the response as UNAUTHORIZED if the access token provided is invalid")
        void setsTheResponseAsUnauthorizedIfTheAccessTokenProvidedIsInvalid() throws IOException, ServletException, AccessTokenInvalidException {
            MockHttpServletRequest req = new MockHttpServletRequest();
            MockHttpServletResponse res = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiZWNydXRlYWsiLCJlbWFpbCI6InRpZXJuZWJyZUBnbWFpbC5jb20ifQ.QCe0mYNZXYyDFF7vYlkGclYBLV-ml0kCQdBDi5wFDo0";
            req.addHeader("Authorization", "Bearer " + token);
            String fingerprint = UUID.randomUUID().toString();
            Cookie[] cookies = { new Cookie(FINGERPRINT_COOKIE_NAME, fingerprint) };
            req.setCookies(cookies);
            when(tokenProvider.validateOne(eq(token), eq(fingerprint))).thenThrow(new AccessTokenInvalidException());
            jwtAuthorizationFilter.doFilterInternal(req, res, filterChain);
            assertEquals(SC_UNAUTHORIZED, res.getStatus());
        }

        @NullSource
        @EmptySource
        @ParameterizedTest(name = "uses null for the fingerprint if the cookies are {0}")
        void usesNullForTheFingerprintIfTheCookiesAre(Cookie[] cookies) throws IOException, ServletException, AccessTokenInvalidException {
            MockHttpServletRequest req = new MockHttpServletRequest();
            MockHttpServletResponse res = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiZWNydXRlYWsiLCJlbWFpbCI6InRpZXJuZWJyZUBnbWFpbC5jb20ifQ.QCe0mYNZXYyDFF7vYlkGclYBLV-ml0kCQdBDi5wFDo0";
            req.addHeader("Authorization", "Bearer " + token);
            req.setCookies(cookies);
            UserDto authorizedUser = UserFactory.generateOneDto();
            when(tokenProvider.validateOne(eq(token), isNull())).thenReturn(authorizedUser);
            jwtAuthorizationFilter.doFilterInternal(req, res, filterChain);
            assertEquals(authorizedUser, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }

        @Test
        @DisplayName("uses null for the fingerprint if the cookies do not contain the fingerprint cookie")
        void usesNullForTheFingerprintIfTheCookiesDoNotContainTheFingerprintCookie() throws IOException, ServletException, AccessTokenInvalidException {
            MockHttpServletRequest req = new MockHttpServletRequest();
            MockHttpServletResponse res = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiZWNydXRlYWsiLCJlbWFpbCI6InRpZXJuZWJyZUBnbWFpbC5jb20ifQ.QCe0mYNZXYyDFF7vYlkGclYBLV-ml0kCQdBDi5wFDo0";
            req.addHeader("Authorization", "Bearer " + token);
            Cookie[] cookies = { new Cookie(UUID.randomUUID().toString(), UUID.randomUUID().toString()) };
            req.setCookies(cookies);
            UserDto authorizedUser = UserFactory.generateOneDto();
            when(tokenProvider.validateOne(eq(token), isNull())).thenReturn(authorizedUser);
            jwtAuthorizationFilter.doFilterInternal(req, res, filterChain);
            assertEquals(authorizedUser, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }

        private void assertSecurityContextHolderDoesNotGetSetForToken(String token) throws IOException, ServletException {
            MockHttpServletRequest req = new MockHttpServletRequest();
            MockHttpServletResponse res = new MockHttpServletResponse();
            MockFilterChain filterChain = new MockFilterChain();
            req.addHeader("Authorization", token);
            jwtAuthorizationFilter.doFilterInternal(req, res, filterChain);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }
    }
}
