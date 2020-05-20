package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.test.WebControllerIntegrationTestSuite;
import com.tiernebre.tailgate.token.TokenFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.UUID;

import static com.tiernebre.tailgate.session.SessionRestfulController.REFRESH_TOKEN_COOKIE_NAME;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SessionRestfulController.class)
public class SessionRestfulControllerIntegrationTests extends WebControllerIntegrationTestSuite {
    @MockBean
    private SessionService sessionService;

    @Nested
    @DisplayName("POST /sessions")
    public class PostTokensTests {
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
        @DisplayName("returns with the token in the JSON response body")
        public void returnsWithTheToken() throws Exception {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            String expectedAccessToken = UUID.randomUUID().toString();
            String expectedRefreshToken = UUID.randomUUID().toString();
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
                    .andExpect(jsonPath("$.refreshToken").value(expectedRefreshToken));
        }

        @Test
        @DisplayName("returns with the refresh token set as a cookie")
        public void returnsWithTheRefreshTokenAsACookie() throws Exception {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            String expectedAccessToken = UUID.randomUUID().toString();
            String expectedRefreshToken = UUID.randomUUID().toString();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(SessionDto.builder()
                    .accessToken(expectedAccessToken)
                    .refreshToken(expectedRefreshToken)
                    .build());
            String expectedCookieHeader = String.format("%s=%s; HttpOnly", REFRESH_TOKEN_COOKIE_NAME, expectedRefreshToken);
            mockMvc.perform(
                    post("/sessions")
                            .content(objectMapper.writeValueAsString(createSessionRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(header().string("Set-Cookie", expectedCookieHeader))
                    .andExpect(cookie().value(REFRESH_TOKEN_COOKIE_NAME, expectedRefreshToken))
                    .andExpect(cookie().httpOnly(REFRESH_TOKEN_COOKIE_NAME, true));
        }
    }
}
