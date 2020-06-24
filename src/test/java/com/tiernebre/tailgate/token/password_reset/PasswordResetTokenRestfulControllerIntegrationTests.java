package com.tiernebre.tailgate.token.password_reset;

import com.tiernebre.tailgate.security_questions.SecurityQuestionDto;
import com.tiernebre.tailgate.security_questions.SecurityQuestionFactory;
import com.tiernebre.tailgate.security_questions.SecurityQuestionService;
import com.tiernebre.tailgate.test.WebControllerIntegrationTestSuite;
import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.service.UserPasswordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PasswordResetTokenRestfulController.class)
public class PasswordResetTokenRestfulControllerIntegrationTests extends WebControllerIntegrationTestSuite {
    @MockBean
    private UserPasswordService userPasswordService;

    @MockBean
    private SecurityQuestionService securityQuestionService;

    @Nested
    @DisplayName("PATCH /password-reset-tokens/{passwordResetToken}/password")
    class PatchPasswordResetTokensPasswordTests {
        @Test
        @DisplayName("returns with 204 NO CONTENT status")
        void returnsWith204NoContentStatus() throws Exception {
            String resetToken = UUID.randomUUID().toString();
            String uri = String.format("/password-reset-tokens/%s/password", resetToken);
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequestFactory.generateOneDto();
            doNothing().when(userPasswordService).updateOneUsingResetToken(eq(resetToken), eq(resetTokenUpdatePasswordRequest));
            mockMvc.perform(
                    patch(uri)
                        .content(objectMapper.writeValueAsString(resetTokenUpdatePasswordRequest))
                        .contentType(APPLICATION_JSON_VALUE)
            ).andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("GET /password-reset-tokens/{passwordResetToken}/security-questions")
    class GetPasswordResetTokenSecurityQuestions {
        @Test
        @DisplayName("returns with 200 OK status")
        void returnsWith204NoContentStatus() throws Exception {
            String resetToken = UUID.randomUUID().toString();
            String uri = String.format("/password-reset-tokens/%s/security-questions", resetToken);
            List<SecurityQuestionDto> expectedSecurityQuestions = SecurityQuestionFactory.generateMultipleDtos();
            when(securityQuestionService.getAllForPasswordResetToken(eq(resetToken))).thenReturn(expectedSecurityQuestions);
            mockMvc.perform(get(uri)).andExpect(status().isOk());
        }
    }
}
