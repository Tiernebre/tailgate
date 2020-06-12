package com.tiernebre.tailgate.password;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password-resets")
public class PasswordResetRestfulController {
    private final PasswordResetService passwordResetService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        passwordResetService.createOne(passwordResetRequest);
    }
}
