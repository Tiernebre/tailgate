package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.test.WebControllerIntegrationTestSuite;
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

@WebMvcTest(controllers = TokenRestfulController.class)
public class TokenRestfulControllerIntegrationTests extends WebControllerIntegrationTestSuite {
    @MockBean
    private TokenService tokenService;

    @Nested
    @DisplayName("POST /tokens")
    public class PostTokensTests {
        @Test
        @DisplayName("returns with 201 CREATED status if successful")
        public void returnsWithCreatedStatus() throws Exception {
            CreateTokenRequest createTokenRequest = TokenFactory.generateOneCreateRequest();
            String expectedToken = UUID.randomUUID().toString();
            when(tokenService.createOne(eq(createTokenRequest))).thenReturn(expectedToken);
            mockMvc.perform(
                    post("/tokens")
                            .content(objectMapper.writeValueAsString(createTokenRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("returns with the token in the JSON response body")
        public void returnsWithTheToken() throws Exception {
            CreateTokenRequest createTokenRequest = TokenFactory.generateOneCreateRequest();
            String expectedToken = UUID.randomUUID().toString();
            when(tokenService.createOne(eq(createTokenRequest))).thenReturn(expectedToken);
            mockMvc.perform(
                    post("/tokens")
                            .content(objectMapper.writeValueAsString(createTokenRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(jsonPath("$.token").exists())
                    .andExpect(jsonPath("$.token").value(expectedToken));
        }
    }
}
