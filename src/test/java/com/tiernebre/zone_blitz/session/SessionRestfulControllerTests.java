package com.tiernebre.zone_blitz.session;

import com.tiernebre.zone_blitz.token.access.GenerateAccessTokenException;
import com.tiernebre.zone_blitz.token.refresh.RefreshTokenConfigurationProperties;
import com.tiernebre.zone_blitz.token.TokenFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.tiernebre.zone_blitz.authentication.SessionCookieNames.FINGERPRINT_COOKIE_NAME;
import static com.tiernebre.zone_blitz.authentication.SessionCookieNames.REFRESH_TOKEN_COOKIE_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionRestfulControllerTests {
    @Mock
    private SessionService sessionService;

    @Mock
    private RefreshTokenConfigurationProperties refreshTokenConfigurationProperties;

    @InjectMocks
    private SessionRestfulController sessionRestfulController;

    private final int TEST_REFRESH_TOKEN_EXPIRATION_IN_MINUTES = 2;

    @BeforeEach
    public void setup() {
        when(refreshTokenConfigurationProperties.getExpirationWindowInMinutes()).thenReturn(TEST_REFRESH_TOKEN_EXPIRATION_IN_MINUTES);
    }

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the created session")
        void returnsTheCreatedToken() throws UserNotFoundForSessionException, GenerateAccessTokenException, InvalidCreateSessionRequestException {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            SessionDto expectedSession = SessionFactory.generateOne();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(expectedSession);
            SessionDto gottenSession = sessionRestfulController.createOne(createSessionRequest, new MockHttpServletResponse());
            assertEquals(expectedSession, gottenSession);
        }

        @Test
        @DisplayName("sets a cookie containing the refresh token on the provided response")
        void returnsTheRefreshTokenAsACookieOnTheProvidedResponse() throws UserNotFoundForSessionException, GenerateAccessTokenException, InvalidCreateSessionRequestException {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            SessionDto expectedSession = SessionFactory.generateOne();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(expectedSession);
            MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
            sessionRestfulController.createOne(createSessionRequest, httpServletResponse);
            Cookie refreshTokenCookie = httpServletResponse.getCookie(REFRESH_TOKEN_COOKIE_NAME);
            assertNotNull(refreshTokenCookie);
            assertEquals(expectedSession.getRefreshToken().toString(), refreshTokenCookie.getValue());
            assertTrue(refreshTokenCookie.isHttpOnly());
            assertTrue(refreshTokenCookie.getSecure());
        }

        @Test
        @DisplayName("sets a cookie containing the access token fingerprint on the provided response")
        void returnsTheAccessTokenFingerprintAsACookieOnTheProvidedResponse() throws UserNotFoundForSessionException, GenerateAccessTokenException, InvalidCreateSessionRequestException {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            SessionDto expectedSession = SessionFactory.generateOne();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(expectedSession);
            MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
            sessionRestfulController.createOne(createSessionRequest, httpServletResponse);
            Cookie fingerprintCookie = httpServletResponse.getCookie(FINGERPRINT_COOKIE_NAME);
            assertNotNull(fingerprintCookie);
            assertEquals(expectedSession.getFingerprint(), fingerprintCookie.getValue());
            assertTrue(fingerprintCookie.isHttpOnly());
            assertTrue(fingerprintCookie.getSecure());
        }
    }

    @Nested
    @DisplayName("refreshOne")
    public class RefreshOneTests {
        @Test
        @DisplayName("returns the refreshed session")
        void returnsTheRefreshedSession() throws GenerateAccessTokenException, InvalidRefreshSessionRequestException {
            UUID refreshToken = UUID.randomUUID();
            SessionDto expectedRefreshedSession = SessionFactory.generateOne();
            when(sessionService.refreshOne(refreshToken)).thenReturn(expectedRefreshedSession);
            SessionDto refreshedSessionGotten = sessionRestfulController.refreshOne(refreshToken, new MockHttpServletResponse());
            assertEquals(expectedRefreshedSession, refreshedSessionGotten);
        }

        @Test
        @DisplayName("sets a cookie with the newly generated refresh token")
        void setsACookieWithTheNewlyGeneratedRefreshToken() throws GenerateAccessTokenException, InvalidRefreshSessionRequestException {
            UUID refreshToken = UUID.randomUUID();
            String expectedNewAccessToken = UUID.randomUUID().toString();
            UUID expectedNewRefreshToken = UUID.randomUUID();
            SessionDto expectedRefreshedSession = SessionDto.builder()
                    .accessToken(expectedNewAccessToken)
                    .refreshToken(expectedNewRefreshToken)
                    .build();
            when(sessionService.refreshOne(refreshToken)).thenReturn(expectedRefreshedSession);
            MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
            sessionRestfulController.refreshOne(refreshToken, mockHttpServletResponse);
            Cookie refreshTokenCookie = mockHttpServletResponse.getCookie(REFRESH_TOKEN_COOKIE_NAME);
            assertNotNull(refreshTokenCookie);
            assertEquals(expectedNewRefreshToken.toString(), refreshTokenCookie.getValue());
            assertTrue(refreshTokenCookie.isHttpOnly());
            assertTrue(refreshTokenCookie.getSecure());
            int expectedCookieAge = Math.toIntExact(TimeUnit.MINUTES.toSeconds(TEST_REFRESH_TOKEN_EXPIRATION_IN_MINUTES));
            assertEquals(expectedCookieAge, refreshTokenCookie.getMaxAge());
        }

        @Test
        @DisplayName("sets a cookie containing the access token fingerprint on the provided response")
        void returnsTheAccessTokenFingerprintAsACookieOnTheProvidedResponse() throws GenerateAccessTokenException, InvalidRefreshSessionRequestException {
            UUID refreshToken = UUID.randomUUID();
            SessionDto expectedRefreshedSession = SessionFactory.generateOne();
            when(sessionService.refreshOne(refreshToken)).thenReturn(expectedRefreshedSession);
            MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
            sessionRestfulController.refreshOne(refreshToken, mockHttpServletResponse);
            Cookie fingerprintCookie = mockHttpServletResponse.getCookie(FINGERPRINT_COOKIE_NAME);
            assertNotNull(fingerprintCookie);
            assertEquals(expectedRefreshedSession.getFingerprint(), fingerprintCookie.getValue());
            assertTrue(fingerprintCookie.isHttpOnly());
            assertTrue(fingerprintCookie.getSecure());
        }
    }
}
