package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.security_questions.SecurityQuestionDto;
import com.tiernebre.zone_blitz.security_questions.SecurityQuestionFactory;
import com.tiernebre.zone_blitz.security_questions.SecurityQuestionService;
import com.tiernebre.zone_blitz.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.zone_blitz.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.zone_blitz.user.exception.InvalidSecurityQuestionAnswerException;
import com.tiernebre.zone_blitz.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.zone_blitz.user.exception.UserNotFoundForPasswordUpdateException;
import com.tiernebre.zone_blitz.user.service.UserPasswordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetTokenRestfulControllerTests {
    @InjectMocks
    private PasswordResetTokenRestfulController passwordResetTokenRestfulController;

    @Mock
    private UserPasswordService userPasswordService;

    @Mock
    private SecurityQuestionService securityQuestionService;

    @Nested
    @DisplayName("updatePasswordUsingResetToken")
    class UpdatePasswordUsingResetTokenTests {
        @Test
        @DisplayName("passes through the request and params properly")
        void passesThroughTheRequestAndParamsProperly() throws InvalidUpdatePasswordRequestException, InvalidPasswordResetTokenException, UserNotFoundForPasswordUpdateException, InvalidSecurityQuestionAnswerException {
            String resetToken = UUID.randomUUID().toString();
            ResetTokenUpdatePasswordRequest resetTokenUpdatePasswordRequest = ResetTokenUpdatePasswordRequestFactory.generateOneDto();
            passwordResetTokenRestfulController.updatePasswordUsingResetToken(resetToken, resetTokenUpdatePasswordRequest);
            verify(userPasswordService, times(1)).updateOneUsingResetToken(eq(resetToken), eq(resetTokenUpdatePasswordRequest));
        }
    }

    @Nested
    @DisplayName("getSecurityQuestionsForPasswordResetToken")
    class GetSecurityQuestionsForPasswordResetTokenTests {
        @Test
        @DisplayName("returns the found security questions")
        void returnsTheFoundSecurityQuestions() {
            String resetToken = UUID.randomUUID().toString();
            List<SecurityQuestionDto> expectedSecurityQuestions = SecurityQuestionFactory.generateMultipleDtos();
            when(securityQuestionService.getAllForPasswordResetToken(eq(resetToken))).thenReturn(expectedSecurityQuestions);
            List<SecurityQuestionDto> gottenSecurityQuestions = passwordResetTokenRestfulController.getSecurityQuestionsForPasswordResetToken(resetToken);
            assertEquals(expectedSecurityQuestions, gottenSecurityQuestions);
        }
    }
}
