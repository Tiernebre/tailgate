package com.tiernebre.tailgate.user;

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
            UserDTO expectedUser = UserFactory.generateOneDto();
            when(userService.createOne(eq(createUserRequest))).thenReturn(expectedUser);
            UserDTO createdUser = userRestfulController.createUser(createUserRequest);
            assertEquals(expectedUser, createdUser);
        }
    }
}
