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

import static com.tiernebre.zone_blitz.session.SessionRestfulController.REFRESH_TOKEN_COOKIE_NAME;
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

    private static final int TEST_REFRESH_TOKEN_EXPIRATION_WINDOW_IN_MINUTES = 1;

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
            String expectedToken = UUID.randomUUID().toString();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(SessionDto.builder().accessToken(expectedToken).build());
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
            String expectedAccessToken = UUID.randomUUID().toString();
            UUID expectedRefreshToken = UUID.randomUUID();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(SessionDto.builder()
                    .accessToken(expectedAccessToken)
                    .refreshToken(expectedRefreshToken)
                    .build());
            mockMvc.perform(
                    post("/sessions")
                            .content(objectMapper.writeValueAsString(createSessionRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(jsonPath("$.accessToken").value(expectedAccessToken))
                    .andExpect(jsonPath("$.refreshToken").exists())
                    .andExpect(jsonPath("$.refreshToken").value(expectedRefreshToken.toString()));
        }

        @Test
        @DisplayName("returns with the refresh token set as a cookie")
        public void returnsWithTheRefreshTokenAsACookie() throws Exception {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            String expectedAccessToken = UUID.randomUUID().toString();
            UUID expectedRefreshToken = UUID.randomUUID();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(SessionDto.builder()
                    .accessToken(expectedAccessToken)
                    .refreshToken(expectedRefreshToken)
                    .build());
            int expectedRefreshTokenAge = Math.toIntExact(TimeUnit.MINUTES.toSeconds(TEST_REFRESH_TOKEN_EXPIRATION_WINDOW_IN_MINUTES));
            mockMvc.perform(
                    post("/sessions")
                            .content(objectMapper.writeValueAsString(createSessionRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(header().exists("Set-Cookie"))
                    .andExpect(cookie().value(REFRESH_TOKEN_COOKIE_NAME, expectedRefreshToken.toString()))
                    .andExpect(cookie().httpOnly(REFRESH_TOKEN_COOKIE_NAME, true))
                    .andExpect(cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, expectedRefreshTokenAge));
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
            when(sessionService.refreshOne(eq(originalRefreshToken))).thenReturn(SessionDto.builder()
                    .accessToken(UUID.randomUUID().toString())
                    .refreshToken(UUID.randomUUID())
                    .build()
            );
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
            String expectedAccessToken = UUID.randomUUID().toString();
            UUID expectedRefreshToken = UUID.randomUUID();
            when(sessionService.refreshOne(eq(originalRefreshToken))).thenReturn(SessionDto.builder()
                    .accessToken(expectedAccessToken)
                    .refreshToken(expectedRefreshToken)
                    .build());
            Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, originalRefreshToken.toString());
            mockMvc.perform(
                    put("/sessions").cookie(refreshTokenCookie)
            )
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(jsonPath("$.accessToken").value(expectedAccessToken))
                    .andExpect(jsonPath("$.refreshToken").exists())
                    .andExpect(jsonPath("$.refreshToken").value(expectedRefreshToken.toString()));
        }

        @Test
        @DisplayName("returns with the refresh token set as a cookie")
        public void returnsWithTheRefreshTokenAsACookie() throws Exception {
            UUID originalRefreshToken = UUID.randomUUID();
            UUID expectedRefreshToken = UUID.randomUUID();
            when(sessionService.refreshOne(eq(originalRefreshToken))).thenReturn(SessionDto.builder()
                    .accessToken(UUID.randomUUID().toString())
                    .refreshToken(expectedRefreshToken)
                    .build());
            int expectedRefreshTokenAge = Math.toIntExact(TimeUnit.MINUTES.toSeconds(TEST_REFRESH_TOKEN_EXPIRATION_WINDOW_IN_MINUTES));
            Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, originalRefreshToken.toString());
            mockMvc.perform(
                    put("/sessions").cookie(refreshTokenCookie)
            )
                    .andExpect(header().exists("Set-Cookie"))
                    .andExpect(cookie().value(REFRESH_TOKEN_COOKIE_NAME, expectedRefreshToken.toString()))
                    .andExpect(cookie().httpOnly(REFRESH_TOKEN_COOKIE_NAME, true))
                    .andExpect(cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, expectedRefreshTokenAge));
        }
    }
}