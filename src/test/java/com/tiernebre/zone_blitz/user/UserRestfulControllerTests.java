package com.tiernebre.zone_blitz.user;

import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.dto.UserUpdatePasswordRequest;
import com.tiernebre.zone_blitz.user.exception.*;
import com.tiernebre.zone_blitz.user.service.UserConfirmationService;
import com.tiernebre.zone_blitz.user.service.UserPasswordService;
import com.tiernebre.zone_blitz.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRestfulControllerTests {
    @Mock
    private UserService userService;

    @Mock
    private UserConfirmationService userConfirmationService;

    @Mock
    private UserPasswordService passwordService;

    @InjectMocks
    private UserRestfulController userRestfulController;

    @Nested
    @DisplayName("createUser")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the created user")
        void returnsTheCreatedUser() throws InvalidUserException, UserAlreadyExistsException {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            UserDto expectedUser = UserFactory.generateOneDto();
            when(userService.createOne(eq(createUserRequest))).thenReturn(expectedUser);
            UserDto createdUser = userRestfulController.createUser(createUserRequest);
            assertEquals(expectedUser, createdUser);
        }
    }

    @Nested
    @DisplayName("confirmUser")
    public class ConfirmUserTests {
        @Test
        @DisplayName("passes along the correct information")
        void passesAlongTheCorrectInformation() throws UserNotFoundForConfirmationException {
            UUID confirmationToken = UUID.randomUUID();
            userRestfulController.confirmUser(confirmationToken);
            verify(userService, times(1)).confirmOne(eq(confirmationToken));
        }
    }

    @Nested
    @DisplayName("sendConfirmationTokenForAuthenticatedUser")
    public class SendConfirmationTokenForAuthenticatedUserTests {
        @Test
        @DisplayName("passes along the correct information")
        void passesAlongTheCorrectInformation() {
            UserDto user = UserFactory.generateOneDto();
            userRestfulController.sendConfirmationTokenForAuthenticatedUser(user);
            verify(userConfirmationService, times(1)).sendOne(eq(user));
        }
    }

    @Nested
    @DisplayName("updatePasswordForCurrentUser")
    public class UpdatePasswordForCurrentUserTests {
        @Test
        @DisplayName("passes along the correct information")
        void passesAlongTheCorrectInformation() throws UserNotFoundForPasswordUpdateException, InvalidUpdatePasswordRequestException {
            UserDto user = UserFactory.generateOneDto();
            UserUpdatePasswordRequest updatePasswordRequest = UpdatePasswordRequestFactory.generateOne();
            userRestfulController.updatePasswordForCurrentUser(user, updatePasswordRequest);
            verify(passwordService, times(1)).updateOneForUser(eq(user), eq(updatePasswordRequest));
        }
    }
}
