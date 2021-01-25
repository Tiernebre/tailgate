package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.security_questions.SecurityQuestionDto;
import com.tiernebre.zone_blitz.security_questions.SecurityQuestionFactory;
import com.tiernebre.zone_blitz.security_questions.SecurityQuestionService;
import com.tiernebre.zone_blitz.test.WebControllerIntegrationTestSuite;
import com.tiernebre.zone_blitz.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.zone_blitz.user.service.UserPasswordService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
            UUID resetToken = UUID.randomUUID();
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
    class GetPasswordResetTokenSecurityQuestion {
        @Test
        @DisplayName("returns with 200 OK status")
        void returnsWith204NoContentStatus() throws Exception {
            UUID resetToken = UUID.randomUUID();
            String uri = String.format("/password-reset-tokens/%s/security-questions", resetToken);
            List<SecurityQuestionDto> expectedSecurityQuestion = SecurityQuestionFactory.generateMultipleDtos();
            when(securityQuestionService.getAllForPasswordResetToken(eq(resetToken))).thenReturn(expectedSecurityQuestion);
            mockMvc.perform(get(uri)).andExpect(status().isOk());
        }

        @Test
        @DisplayName("returns with the security questions properly formatted in JSON")
        void returnsWithTheSecurityQuestionProperlyFormattedInJson() throws Exception {
            UUID resetToken = UUID.randomUUID();
            String uri = String.format("/password-reset-tokens/%s/security-questions", resetToken);
            List<SecurityQuestionDto> expectedSecurityQuestion = SecurityQuestionFactory.generateMultipleDtos();
            SecurityQuestionDto firstSecurityQuestionDto = expectedSecurityQuestion.get(0);
            SecurityQuestionDto secondSecurityQuestionDto = expectedSecurityQuestion.get(1);
            when(securityQuestionService.getAllForPasswordResetToken(eq(resetToken))).thenReturn(expectedSecurityQuestion);
            mockMvc.perform(get(uri))
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].id").value(firstSecurityQuestionDto.getId()))
                    .andExpect(jsonPath("$[0].question").exists())
                    .andExpect(jsonPath("$[0].question").value(firstSecurityQuestionDto.getQuestion()))
                    .andExpect(jsonPath("$[1].id").exists())
                    .andExpect(jsonPath("$[1].id").value(secondSecurityQuestionDto.getId()))
                    .andExpect(jsonPath("$[1].question").exists())
                    .andExpect(jsonPath("$[1].question").value(secondSecurityQuestionDto.getQuestion()));
        }
    }
}
