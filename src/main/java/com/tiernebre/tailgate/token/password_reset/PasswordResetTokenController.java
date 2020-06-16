package com.tiernebre.tailgate.token.password_reset;

import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidPasswordResetTokenException;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.tailgate.user.exception.UserNotFoundForPasswordUpdateException;
import com.tiernebre.tailgate.user.service.UserPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("password-reset-tokens")
@RequiredArgsConstructor
public class PasswordResetTokenController {
    private final UserPasswordService userPasswordService;

    @PatchMapping("/{passwordResetToken}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePasswordUsingResetToken(
            @RequestParam String passwordResetToken,
            @RequestBody ResetTokenUpdatePasswordRequest request
    ) throws UserNotFoundForPasswordUpdateException, InvalidPasswordResetTokenException, InvalidUpdatePasswordRequestException {
        userPasswordService.updateOneUsingResetToken(passwordResetToken, request);
    }
}
