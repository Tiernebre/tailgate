package com.tiernebre.zone_blitz.user;

import com.tiernebre.zone_blitz.authentication.IsAuthenticated;
import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.dto.UserUpdatePasswordRequest;
import com.tiernebre.zone_blitz.user.exception.*;
import com.tiernebre.zone_blitz.user.service.UserConfirmationService;
import com.tiernebre.zone_blitz.user.service.UserPasswordService;
import com.tiernebre.zone_blitz.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestfulController {
    private final UserService service;
    private final UserConfirmationService confirmationService;
    private final UserPasswordService passwordService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody CreateUserRequest createRequest) throws InvalidUserException, UserAlreadyExistsException {
        return service.createOne(createRequest);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping("/confirmation/{confirmationToken}")
    public void confirmUser(@PathVariable UUID confirmationToken) throws UserNotFoundForConfirmationException {
        service.confirmOne(confirmationToken);
    }

    @IsAuthenticated
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping("/me/confirmation-token")
    public void sendConfirmationTokenForAuthenticatedUser(@AuthenticationPrincipal UserDto authenticatedUser) {
        confirmationService.sendOne(authenticatedUser);
    }

    @IsAuthenticated
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping("/me/password")
    public void updatePasswordForCurrentUser(
            @AuthenticationPrincipal UserDto authenticatedUser,
            @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest
    ) throws UserNotFoundForPasswordUpdateException, InvalidUpdatePasswordRequestException {
        passwordService.updateOneForUser(authenticatedUser, userUpdatePasswordRequest);
    }
}
