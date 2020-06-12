package com.tiernebre.tailgate.password;

import com.tiernebre.tailgate.test.WebControllerIntegrationTestSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PasswordResetRestfulController.class)
public class PasswordResetRestfulControllerIntegrationTests extends WebControllerIntegrationTestSuite {
    @MockBean
    private PasswordResetService passwordResetService;

    @DisplayName("POST /password-resets")
    @Nested
    public class PostPasswordResets {
        @Test
        @DisplayName("returns with 204 NO CONTENT response status")
        public void returnsWithNoContentResponseStatus () throws Exception {
            PasswordResetRequest passwordResetRequest = PasswordResetFactory.generateOneRequest();
            doNothing().when(passwordResetService).createOne(eq(passwordResetRequest));
            mockMvc.perform(
                    post("/password-resets")
                            .content(objectMapper.writeValueAsString(passwordResetRequest))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andExpect(status().isNoContent());
        }
    }
}
