package com.tiernebre.zone_blitz.session;

import com.tiernebre.zone_blitz.test.WebControllerIntegrationTestSuite;
import com.tiernebre.zone_blitz.token.refresh.RefreshTokenConfigurationProperties;
import com.tiernebre.zone_blitz.token.TokenFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import javax.servlet.http.Cookie;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.tiernebre.zone_blitz.authentication.SessionCookieNames.FINGERPRINT_COOKIE_NAME;
import static com.tiernebre.zone_blitz.authentication.SessionCookieNames.REFRESH_TOKEN_COOKIE_NAME;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SessionRestfulController.class)
public class SessionRestfulControllerIntegrationTests extends WebControllerIntegrationTestSuite {
    @MockBean
    private SessionService sessionService;

    @MockBean
    private RefreshTokenConfigurationProperties refreshTokenConfigurationProperties;

    private static final int TEST_REFRESH_TOKEN_EXPIRATION_WINDOW_IN_MINUTES = 10;

    @BeforeEach
    public void setup() {
        when(refreshTokenConfigurationProperties.getExpirationWindowInMinutes()).thenReturn(TEST_REFRESH_TOKEN_EXPIRATION_WINDOW_IN_MINUTES);
    }

    @Nested
    @DisplayName("POST /sessions")
    public class PostSessionsTests {
        @Test
        @DisplayName("returns with 201 CREATED status if successful")
        public void returnsWithCreatedStatus() throws Exception {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(SessionFactory.generateOne());
            mockMvc.perform(
                    post("/sessions")
                            .content(objectMapper.writeValueAsString(createSessionRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("returns with the session in the JSON response body")
        public void returnsWithTheSession() throws Exception {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            SessionDto expectedSession = SessionFactory.generateOne();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(expectedSession);
            mockMvc.perform(
                    post("/sessions")
                            .content(objectMapper.writeValueAsString(createSessionRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(jsonPath("$.accessToken").value(expectedSession.getAccessToken()))
                    .andExpect(jsonPath("$.refreshToken").doesNotExist())
                    .andExpect(jsonPath("$.fingerprint").doesNotExist());
        }

        @Test
        @DisplayName("returns with the refresh token set as a cookie")
        public void returnsWithTheRefreshTokenAsACookie() throws Exception {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            SessionDto expectedSession = SessionFactory.generateOne();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(expectedSession);
            int expectedRefreshTokenAge = Math.toIntExact(TimeUnit.MINUTES.toSeconds(TEST_REFRESH_TOKEN_EXPIRATION_WINDOW_IN_MINUTES));
            mockMvc.perform(
                    post("/sessions")
                            .content(objectMapper.writeValueAsString(createSessionRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(header().exists("Set-Cookie"))
                    .andExpect(cookie().value(REFRESH_TOKEN_COOKIE_NAME, expectedSession.getRefreshToken().toString()))
                    .andExpect(cookie().httpOnly(REFRESH_TOKEN_COOKIE_NAME, true))
                    .andExpect(cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, expectedRefreshTokenAge));
        }

        @Test
        @DisplayName("returns with the fingerprint set as a cookie")
        public void returnsWithTheFingerprintSetAsACookie() throws Exception {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            SessionDto expectedSession = SessionFactory.generateOne();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(expectedSession);
            mockMvc.perform(
                    post("/sessions")
                            .content(objectMapper.writeValueAsString(createSessionRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(header().exists("Set-Cookie"))
                    .andExpect(cookie().value(FINGERPRINT_COOKIE_NAME, expectedSession.getFingerprint()))
                    .andExpect(cookie().secure(FINGERPRINT_COOKIE_NAME, true))
                    .andExpect(cookie().httpOnly(FINGERPRINT_COOKIE_NAME, true));
        }
    }

    @Nested
    @DisplayName("PUT /sessions")
    @WithMockUser
    public class PutSessionsTests {
        @Test
        @DisplayName("returns with 201 CREATED status if successful")
        public void returnsWithCreatedStatus() throws Exception {
            UUID originalRefreshToken = UUID.randomUUID();
            when(sessionService.refreshOne(eq(originalRefreshToken))).thenReturn(SessionFactory.generateOne());
            Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, originalRefreshToken.toString());
            mockMvc.perform(
                    put("/sessions").cookie(refreshTokenCookie)
            )
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("returns with 400 BAD REQUEST status if no refresh token is provided")
        public void returnsWithBadRequestStatusIfNoRefreshTokenIsProvided() throws Exception {
            mockMvc.perform(
                    put("/sessions")
            )
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("returns with the session in the JSON response body")
        public void returnsWithTheSession() throws Exception {
            UUID originalRefreshToken = UUID.randomUUID();
            SessionDto expectedSession = SessionFactory.generateOne();
            when(sessionService.refreshOne(eq(originalRefreshToken))).thenReturn(expectedSession);
            Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, originalRefreshToken.toString());
            mockMvc.perform(
                    put("/sessions").cookie(refreshTokenCookie)
            )
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(jsonPath("$.accessToken").value(expectedSession.getAccessToken()))
                    .andExpect(jsonPath("$.refreshToken").doesNotExist())
                    .andExpect(jsonPath("$.fingerprint").doesNotExist());
        }

        @Test
        @DisplayName("returns with the refresh token set as a cookie")
        public void returnsWithTheRefreshTokenAsACookie() throws Exception {
            UUID originalRefreshToken = UUID.randomUUID();
            SessionDto expectedSession = SessionFactory.generateOne();
            when(sessionService.refreshOne(eq(originalRefreshToken))).thenReturn(expectedSession);
            int expectedRefreshTokenAge = Math.toIntExact(TimeUnit.MINUTES.toSeconds(TEST_REFRESH_TOKEN_EXPIRATION_WINDOW_IN_MINUTES));
            Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, originalRefreshToken.toString());
            mockMvc.perform(
                    put("/sessions").cookie(refreshTokenCookie)
            )
                    .andExpect(header().exists("Set-Cookie"))
                    .andExpect(cookie().value(REFRESH_TOKEN_COOKIE_NAME, expectedSession.getRefreshToken().toString()))
                    .andExpect(cookie().httpOnly(REFRESH_TOKEN_COOKIE_NAME, true))
                    .andExpect(cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, expectedRefreshTokenAge));
        }

        @Test
        @DisplayName("returns with the fingerprint set as a cookie")
        public void returnsWithTheFingerprintAsACookie() throws Exception {
            UUID originalRefreshToken = UUID.randomUUID();
            SessionDto expectedSession = SessionFactory.generateOne();
            when(sessionService.refreshOne(eq(originalRefreshToken))).thenReturn(expectedSession);
            Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, originalRefreshToken.toString());
            mockMvc.perform(
                    put("/sessions").cookie(refreshTokenCookie)
            )
                    .andExpect(header().exists("Set-Cookie"))
                    .andExpect(cookie().value(FINGERPRINT_COOKIE_NAME, expectedSession.getFingerprint()))
                    .andExpect(cookie().secure(FINGERPRINT_COOKIE_NAME, true))
                    .andExpect(cookie().httpOnly(FINGERPRINT_COOKIE_NAME, true));
        }
    }
}
