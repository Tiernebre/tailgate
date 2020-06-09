package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.exception.InvalidUserException;
import com.tiernebre.tailgate.user.exception.UserAlreadyExistsException;
import com.tiernebre.tailgate.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRestfulControllerTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserRestfulController userRestfulController;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        void testItReturnsTheCreatedUser() throws InvalidUserException, UserAlreadyExistsException {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            UserDto expectedUser = UserFactory.generateOneDto();
            when(userService.createOne(eq(createUserRequest))).thenReturn(expectedUser);
            UserDto createdUser = userRestfulController.createUser(createUserRequest);
            assertEquals(expectedUser, createdUser);
        }
    }
}
