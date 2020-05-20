package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.GenerateAccessTokenException;
import com.tiernebre.tailgate.token.TokenFactory;
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

import static com.tiernebre.tailgate.session.SessionRestfulController.REFRESH_TOKEN_COOKIE_NAME;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionRestfulControllerTests {
    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionRestfulController sessionRestfulController;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the created session")
        void returnsTheCreatedToken() throws UserNotFoundForSessionException, GenerateAccessTokenException, InvalidCreateSessionRequestException {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            String expectedAccessToken = UUID.randomUUID().toString();
            String expectedRefreshToken = UUID.randomUUID().toString();
            SessionDto expectedSession = SessionDto.builder()
                    .accessToken(expectedAccessToken)
                    .refreshToken(expectedRefreshToken)
                    .build();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(expectedSession);
            SessionDto gottenSession = sessionRestfulController.createOne(createSessionRequest, new MockHttpServletResponse());
            assertEquals(expectedSession, gottenSession);
        }

        @Test
        @DisplayName("sets a cookie containing the refresh token on the provided response")
        void returnsTheRefreshTokenAsACookieOnTheProvidedResponse() throws UserNotFoundForSessionException, GenerateAccessTokenException, InvalidCreateSessionRequestException {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            String expectedAccessToken = UUID.randomUUID().toString();
            String expectedRefreshToken = UUID.randomUUID().toString();
            SessionDto expectedSession = SessionDto.builder()
                    .accessToken(expectedAccessToken)
                    .refreshToken(expectedRefreshToken)
                    .build();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(expectedSession);
            MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
            sessionRestfulController.createOne(createSessionRequest, httpServletResponse);
            Cookie refreshTokenCookie = httpServletResponse.getCookie(REFRESH_TOKEN_COOKIE_NAME);
            assertNotNull(refreshTokenCookie);
            assertEquals(expectedRefreshToken, refreshTokenCookie.getValue());
            assertTrue(refreshTokenCookie.isHttpOnly());
        }
    }
    @Nested
    @DisplayName("refreshOne")
    public class RefreshOneTests {
        @Test
        @DisplayName("returns the refreshed session")
        void returnsTheRefreshedSession() throws GenerateAccessTokenException, InvalidRefreshSessionRequestException {
            String refreshToken = UUID.randomUUID().toString();
            String expectedNewAccessToken = UUID.randomUUID().toString();
            String expectedNewRefreshToken = UUID.randomUUID().toString();
            SessionDto expectedRefreshedSession = SessionDto.builder()
                    .accessToken(expectedNewAccessToken)
                    .refreshToken(expectedNewRefreshToken)
                    .build();
            when(sessionService.refreshOne(refreshToken)).thenReturn(expectedRefreshedSession);
            SessionDto refreshedSessionGotten = sessionRestfulController.refreshOne(refreshToken, new MockHttpServletResponse());
            assertEquals(expectedRefreshedSession, refreshedSessionGotten);
        }
    }
}
