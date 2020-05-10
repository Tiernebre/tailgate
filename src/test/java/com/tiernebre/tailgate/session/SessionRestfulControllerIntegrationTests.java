package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.test.WebControllerIntegrationTestSuite;
import com.tiernebre.tailgate.token.AccessTokenService;
import com.tiernebre.tailgate.token.CreateSessionRequest;
import com.tiernebre.tailgate.token.TokenFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SessionRestfulController.class)
public class SessionRestfulControllerIntegrationTests extends WebControllerIntegrationTestSuite {
    @MockBean
    private AccessTokenService accessTokenService;

    @Nested
    @DisplayName("POST /sessions")
    public class PostTokensTests {
        @Test
        @DisplayName("returns with 201 CREATED status if successful")
        public void returnsWithCreatedStatus() throws Exception {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            String expectedToken = UUID.randomUUID().toString();
            when(accessTokenService.createOne(eq(createSessionRequest))).thenReturn(expectedToken);
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
            String expectedToken = UUID.randomUUID().toString();
            when(accessTokenService.createOne(eq(createSessionRequest))).thenReturn(expectedToken);
            mockMvc.perform(
                    post("/sessions")
                            .content(objectMapper.writeValueAsString(createSessionRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(jsonPath("$.accessToken").value(expectedToken));
        }
    }
}
