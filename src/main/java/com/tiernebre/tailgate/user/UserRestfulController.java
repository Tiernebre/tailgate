package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.authentication.IsAuthenticated;
import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.dto.UserUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.*;
import com.tiernebre.tailgate.user.service.UserConfirmationService;
import com.tiernebre.tailgate.user.service.UserPasswordService;
import com.tiernebre.tailgate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public void confirmUser(@PathVariable String confirmationToken) throws UserNotFoundForConfirmationException {
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
