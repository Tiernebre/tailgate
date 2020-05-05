package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.user.validator.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {
    @Mock
    private UserRepository repository;

    @Mock
    private UserConverter userConverter;

    @Mock
    private UserValidator userValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the created User as a DTO")
        void testCreateOneReturnsProperly() throws InvalidUserException, UserAlreadyExistsException {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            UserEntity entityFromCreateUserRequest = UserFactory.generateOneEntity();
            String encryptedPassword = "abcd12345!WOW";
            when(passwordEncoder.encode(createUserRequest.getPassword())).thenReturn(encryptedPassword);
            CreateUserRequest encryptedUserRequest = createUserRequest.withPassword(encryptedPassword);
            when(userConverter.convertFromCreateRequest(eq(encryptedUserRequest))).thenReturn(entityFromCreateUserRequest);
            UserEntity entitySaved = UserFactory.generateOneEntity();
            when(repository.saveOne(eq(entityFromCreateUserRequest))).thenReturn(entitySaved);
            UserDto expectedUserCreated = UserFactory.generateOneDto();
            when(userConverter.convertFromEntity(eq(entitySaved))).thenReturn(expectedUserCreated);
            doNothing().when(userValidator).validate(eq(createUserRequest));
            when(repository.oneExistsByEmail(eq(createUserRequest.getEmail()))).thenReturn(false);
            UserDto gottenUserCreated = userService.createOne(createUserRequest);
            assertEquals(expectedUserCreated, gottenUserCreated);
        }

        @Test
        @DisplayName("throws an InvalidUserException if the request was not valid")
        void testCreateOneThrowsInvalidUserExceptionIfValidatorFails() throws InvalidUserException {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            doThrow(new InvalidUserException(Collections.emptySet())).when(userValidator).validate(eq(createUserRequest));
            assertThrows(InvalidUserException.class, () -> userService.createOne(createUserRequest));
        }

        @Test
        @DisplayName("throws a NullPointerException if the create user request is null")
        void testCreateOneThrowsNullPointerExceptionIfTheRequestIsNull() throws InvalidUserException {
            NullPointerException thrownException = assertThrows(NullPointerException.class, () -> userService.createOne(null));
            assertEquals("The create user request is a required parameter and must not be null", thrownException.getMessage());
        }

        @Test
        @DisplayName("throws a UserAlreadyExistsException if the email already exists")
        void throwsUserAlreadyExistsExceptionIfEmailExists() throws InvalidUserException {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            doNothing().when(userValidator).validate(eq(createUserRequest));
            when(repository.oneExistsByEmail(eq(createUserRequest.getEmail()))).thenReturn(true);
            assertThrows(UserAlreadyExistsException.class, () -> userService.createOne(createUserRequest));
        }
    }

    @Nested
    @DisplayName("findOneByEmailAndPassword")
    public class FindOneByEmailAndPasswordTests {
        @Test
        @DisplayName("returns an optional containing the found user if the email exists and the passwords match")
        void testReturnsAnOptionalIfEmailExistsAndPasswordsMatch() {
            String email = UUID.randomUUID().toString() + "@test.com";
            String password = "testPassword12345!";
            UserEntity foundUserEntity = UserFactory.generateOneEntity();
            UserDto expectedUser = UserFactory.generateOneDto();
            when(repository.findOneByEmail(eq(email))).thenReturn(Optional.of(foundUserEntity));
            when(passwordEncoder.matches(eq(password), eq(foundUserEntity.getPassword()))).thenReturn(true);
            when(userConverter.convertFromEntity((foundUserEntity))).thenReturn(expectedUser);
            UserDto gottenUser = userService.findOneByEmailAndPassword(email, password).orElse(null);
            assertNotNull(gottenUser);
            assertEquals(expectedUser, gottenUser);
        }

        @Test
        @DisplayName("returns an empty optional if the email does not exist")
        void testReturnsAnEmptyOptionalIfTheEmailDoesNotExist() {
            String email = UUID.randomUUID().toString() + "@test.com";
            String password = "testPassword12345!";
            when(repository.findOneByEmail(eq(email))).thenReturn(Optional.empty());
            Optional<UserDto> gottenUser = userService.findOneByEmailAndPassword(email, password);
            assertTrue(gottenUser.isEmpty());
        }

        @Test
        @DisplayName("returns an empty optional if the email exists, but the passwords do not match")
        void testReturnsAnEmptyOptionalIfTheEmailExistsButThePasswordsDoNotMatch() {
            String email = UUID.randomUUID().toString() + "@test.com";
            String password = "testPassword12345!";
            UserEntity foundUserEntity = UserFactory.generateOneEntity();
            when(repository.findOneByEmail(eq(email))).thenReturn(Optional.of(foundUserEntity));
            when(passwordEncoder.matches(eq(password), eq(foundUserEntity.getPassword()))).thenReturn(false);
            Optional<UserDto> gottenUser = userService.findOneByEmailAndPassword(email, password);
            assertTrue(gottenUser.isEmpty());
        }

        @Test
        @DisplayName("throws a NullPointerException if the email is null")
        void testThrowsNullPointerExceptionIfEmailIsNull() throws InvalidUserException {
            NullPointerException thrownException = assertThrows(NullPointerException.class, () -> userService.findOneByEmailAndPassword(null, UUID.randomUUID().toString()));
            assertEquals("The email to find a user for is a required parameter and must not be null", thrownException.getMessage());
        }

        @Test
        @DisplayName("throws a NullPointerException if the password is null")
        void testThrowsNullPointerExceptionIfPasswordIsNull() throws InvalidUserException {
            NullPointerException thrownException = assertThrows(NullPointerException.class, () -> userService.findOneByEmailAndPassword(UUID.randomUUID().toString(), null));
            assertEquals("The password to find a user for is a required parameter and must not be null", thrownException.getMessage());
        }
    }
}
