package com.tiernebre.tailgate.security_questions;

import com.tiernebre.tailgate.test.WebControllerIntegrationTestSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SecurityQuestionRestfulController.class)
public class SecurityQuestionRestfulControllerIntegrationTests extends WebControllerIntegrationTestSuite {
    @MockBean
    private SecurityQuestionService securityQuestionService;

    @Nested
    @DisplayName("GET /security-questions")
    public class GetSecurityQuestionsTests {
        @Test
        @DisplayName("returns with 200 OK status")
        public void returnsWithOkStatus() throws Exception {
            when(securityQuestionService.getAll()).thenReturn(SecurityQuestionFactory.generateMultipleDtos());
            mockMvc.perform(get("/security-questions"))
                    .andExpect(status().isOk());
        }
    }
}
