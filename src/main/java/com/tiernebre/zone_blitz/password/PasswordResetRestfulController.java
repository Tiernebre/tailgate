package com.tiernebre.zone_blitz.password;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password-resets")
@RequiredArgsConstructor
public class PasswordResetRestfulController {
    private final PasswordResetService passwordResetService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        passwordResetService.createOne(passwordResetRequest);
    }
}
