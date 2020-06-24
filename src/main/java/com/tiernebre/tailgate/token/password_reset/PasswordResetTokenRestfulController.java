package com.tiernebre.tailgate.token.password_reset;

import com.tiernebre.tailgate.security_questions.SecurityQuestionDto;
import com.tiernebre.tailgate.security_questions.SecurityQuestionService;
import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.tailgate.user.exception.InvalidSecurityQuestionAnswerException;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.tailgate.user.exception.UserNotFoundForPasswordUpdateException;
import com.tiernebre.tailgate.user.service.UserPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("password-reset-tokens")
@RequiredArgsConstructor
public class PasswordResetTokenRestfulController {
    private final UserPasswordService userPasswordService;
    private final SecurityQuestionService securityQuestionService;

    @PatchMapping("/{passwordResetToken}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePasswordUsingResetToken(
            @PathVariable String passwordResetToken,
            @RequestBody ResetTokenUpdatePasswordRequest request
    ) throws UserNotFoundForPasswordUpdateException, InvalidPasswordResetTokenException, InvalidUpdatePasswordRequestException, InvalidSecurityQuestionAnswerException {
        userPasswordService.updateOneUsingResetToken(passwordResetToken, request);
    }

    @GetMapping("/{passwordResetToken}/security-questions")
    public List<SecurityQuestionDto> getSecurityQuestionsForPasswordResetToken(
            @PathVariable String passwordResetToken
    ) {
        return securityQuestionService.getAllForPasswordResetToken(passwordResetToken);
    }
}
