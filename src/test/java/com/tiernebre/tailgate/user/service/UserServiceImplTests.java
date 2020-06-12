package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.CreateUserSecurityQuestionRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.entity.UserEntity;
import com.tiernebre.tailgate.user.exception.InvalidUserException;
import com.tiernebre.tailgate.user.exception.UserAlreadyExistsException;
import com.tiernebre.tailgate.user.repository.UserRepository;
import com.tiernebre.tailgate.user.validator.UserValidator;
import com.tiernebre.tailgate.validator.StringIsBlankException;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static com.tiernebre.tailgate.user.service.UserServiceImpl.*;
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

    @Mock
    private UserConfirmationService confirmationService;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the created User as a DTO")
        void testCreateOneReturnsProperly() throws InvalidUserException, UserAlreadyExistsException {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            String hashedPassword = "abcd12345!WOW";
            List<CreateUserSecurityQuestionRequest> hashedSecurityQuestions = new ArrayList<>();
            assertTrue(CollectionUtils.isNotEmpty(createUserRequest.getSecurityQuestions()));
            for (int i = 0; i < createUserRequest.getSecurityQuestions().size(); i++) {
                CreateUserSecurityQuestionRequest originalRequest = createUserRequest.getSecurityQuestions().get(i);
                String expectedAnswer = UUID.randomUUID().toString();
                when(passwordEncoder.encode(originalRequest.getAnswer().toLowerCase())).thenReturn(expectedAnswer);
                hashedSecurityQuestions.add(CreateUserSecurityQuestionRequest.builder()
                        .id(originalRequest.getId())
                        .answer(expectedAnswer)
                        .build());
            }
            when(passwordEncoder.encode(createUserRequest.getPassword())).thenReturn(hashedPassword);
            CreateUserRequest hashedUserRequest = createUserRequest
                    .withPassword(hashedPassword)
                    .withSecurityQuestions(hashedSecurityQuestions);
            UserEntity entitySaved = UserFactory.generateOneEntity();
            when(repository.createOne(eq(hashedUserRequest))).thenReturn(entitySaved);
            UserDto expectedUserCreated = UserFactory.generateOneDto();
            when(userConverter.convertFromEntity(eq(entitySaved))).thenReturn(expectedUserCreated);
            doNothing().when(userValidator).validate(eq(createUserRequest));
            when(repository.oneExistsByEmail(eq(createUserRequest.getEmail()))).thenReturn(false);
            UserDto gottenUserCreated = userService.createOne(createUserRequest);
            assertEquals(expectedUserCreated, gottenUserCreated);
            verify(confirmationService, times(1)).sendOne(eq(expectedUserCreated));
        }

        @Test
        @DisplayName("throws an InvalidUserException if the request was not valid")
        void testCreateOneThrowsInvalidUserExceptionIfValidatorFails() throws InvalidUserException {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            doThrow(new InvalidUserException(Collections.emptySet())).when(userValidator).validate(eq(createUserRequest));
            assertThrows(InvalidUserException.class, () -> userService.createOne(createUserRequest));
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

        @ParameterizedTest(name = "throws a StringIsBlankException with a helpful error message if the email is \"{0}\"")
        @NullSource
        @ValueSource(strings = {"", " "})
        void testThrowsNullPointerExceptionIfEmailIs(String email) {
            StringIsBlankException thrownException = assertThrows(StringIsBlankException.class, () -> userService.findOneByEmailAndPassword(email, UUID.randomUUID().toString()));
            assertEquals(REQUIRED_EMAIL_MESSAGE, thrownException.getMessage());
        }

        @ParameterizedTest(name = "throws a StringIsBlankException with a helpful error message if the password is \"{0}\"")
        @NullSource
        @ValueSource(strings = {"", " "})
        void testThrowsNullPointerExceptionIfPasswordIs(String password) {
            StringIsBlankException thrownException = assertThrows(StringIsBlankException.class, () -> userService.findOneByEmailAndPassword(UUID.randomUUID().toString(), password));
            assertEquals(REQUIRED_PASSWORD_MESSAGE, thrownException.getMessage());
        }
    }

    @Nested
    @DisplayName("findOneByNonExpiredRefreshToken")
    public class FindOneByNonExpiredRefreshTokenTests {
        @Test
        @DisplayName("returns the optional containing a user from the repository with a given refresh token")
        void testReturnsTheOptionalContainingAUserFromTheRepositoryWithAGivenRefreshToken() {
            String refreshToken = UUID.randomUUID().toString();
            UserEntity expectedUserFound = UserFactory.generateOneEntity();
            when(repository.findOneWithNonExpiredRefreshToken(eq(refreshToken))).thenReturn(Optional.of(expectedUserFound));
            UserDto expectedMappedUser = UserFactory.generateOneDto();
            when(userConverter.convertFromEntity(eq(expectedUserFound))).thenReturn(expectedMappedUser);
            Optional<UserDto> foundUser = userService.findOneByNonExpiredRefreshToken(refreshToken);
            assertTrue(foundUser.isPresent());
            assertEquals(expectedMappedUser, foundUser.get());
        }

        @Test
        @DisplayName("returns an empty optional from the repository with a given refresh token")
        void testReturnsAnEmptyOptionalFromTheRepositoryWithAGivenRefreshToken() {
            String refreshToken = UUID.randomUUID().toString();
            when(repository.findOneWithNonExpiredRefreshToken(eq(refreshToken))).thenReturn(Optional.empty());
            Optional<UserDto> foundUser = userService.findOneByNonExpiredRefreshToken(refreshToken);
            assertFalse(foundUser.isPresent());
        }

        @ParameterizedTest(name = "throws a StringIsBlankException if the refresh token is \"{0}\"")
        @NullSource
        @ValueSource(strings = { "", " " })
        void throwsAStringIsBlankExceptionIfTheRefreshTokenIs(String refreshToken) {
            StringIsBlankException thrownException = assertThrows(StringIsBlankException.class, () -> userService.findOneByNonExpiredRefreshToken(refreshToken));
            assertEquals(REQUIRED_REFRESH_TOKEN_MESSAGE, thrownException.getMessage());
        }
    }

    @Nested
    @DisplayName("findOneByEmail")
    public class FindOneByEmailTests {
        @Test
        @DisplayName("returns an optional containing a mapped user if one exists")
        void returnsAnOptionalContainingAMappedUserIfOneExists() {
            String email = UUID.randomUUID().toString() + ".com";
            UserEntity foundUser = UserFactory.generateOneEntity();
            UserDto expectedUser = UserFactory.generateOneDto();
            when(repository.findOneByEmail(eq(email))).thenReturn(Optional.of(foundUser));
            when(userConverter.convertFromEntity(eq(foundUser))).thenReturn(expectedUser);
            Optional<UserDto> gottenUser = userService.findOneByEmail(email);
            assertTrue(gottenUser.isPresent());
            assertEquals(expectedUser, gottenUser.get());
        }
    }
}
