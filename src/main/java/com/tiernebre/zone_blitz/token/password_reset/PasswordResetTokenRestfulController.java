package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.security_questions.SecurityQuestionDto;
import com.tiernebre.zone_blitz.security_questions.SecurityQuestionService;
import com.tiernebre.zone_blitz.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.zone_blitz.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.zone_blitz.user.exception.InvalidSecurityQuestionAnswerException;
import com.tiernebre.zone_blitz.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.zone_blitz.user.exception.UserNotFoundForPasswordUpdateException;
import com.tiernebre.zone_blitz.user.service.UserPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("password-reset-tokens")
@RequiredArgsConstructor
public class PasswordResetTokenRestfulController {
    private final UserPasswordService userPasswordService;
    private final SecurityQuestionService securityQuestionService;

    @PatchMapping("/{passwordResetToken}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePasswordUsingResetToken(
            @PathVariable UUID passwordResetToken,
            @RequestBody ResetTokenUpdatePasswordRequest request
    ) throws UserNotFoundForPasswordUpdateException, InvalidPasswordResetTokenException, InvalidUpdatePasswordRequestException, InvalidSecurityQuestionAnswerException {
        userPasswordService.updateOneUsingResetToken(passwordResetToken, request);
    }

    @GetMapping("/{passwordResetToken}/security-questions")
    public List<SecurityQuestionDto> getSecurityQuestionForPasswordResetToken(
            @PathVariable UUID passwordResetToken
    ) {
        return securityQuestionService.getAllForPasswordResetToken(passwordResetToken);
    }
}
