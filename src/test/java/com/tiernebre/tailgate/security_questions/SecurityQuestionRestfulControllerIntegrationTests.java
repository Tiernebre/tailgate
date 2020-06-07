package com.tiernebre.tailgate.security_questions;

import com.tiernebre.tailgate.test.WebControllerIntegrationTestSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        @Test
        @DisplayName("returns with the security questions formatted in the json response body")
        public void returnsWithTheSecurityQuestionsFormattedInTheJsonResponseBody() throws Exception {
            List<SecurityQuestionDto> expectedSecurityQuestions = SecurityQuestionFactory.generateMultipleDtos();
            SecurityQuestionDto firstSecurityQuestionDto = expectedSecurityQuestions.get(0);
            when(securityQuestionService.getAll()).thenReturn(expectedSecurityQuestions);
            mockMvc.perform(get("/security-questions"))
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].id").value(firstSecurityQuestionDto.getId()))
                    .andExpect(jsonPath("$[0].question").exists())
                    .andExpect(jsonPath("$[0].question").value(firstSecurityQuestionDto.getQuestion()));
        }
    }
}
