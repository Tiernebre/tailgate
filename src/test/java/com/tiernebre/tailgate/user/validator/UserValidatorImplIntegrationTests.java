package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.exception.InvalidException;
import com.tiernebre.tailgate.security_questions.SecurityQuestionService;
import com.tiernebre.tailgate.test.SpringIntegrationTestingSuite;
import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.CreateUserSecurityQuestionRequest;
import com.tiernebre.tailgate.user.exception.InvalidUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tiernebre.tailgate.test.ValidatorTestUtils.assertThatValidationInvalidatedCorrectly;
import static com.tiernebre.tailgate.user.dto.CreateUserRequest.NUMBER_OF_ALLOWED_SECURITY_QUESTIONS;
import static com.tiernebre.tailgate.user.validator.UserValidatorImpl.NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE;
import static com.tiernebre.tailgate.user.validator.UserValidatorImpl.NULL_CREATE_USER_REQUEST_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class UserValidatorImplIntegrationTests extends SpringIntegrationTestingSuite {
    private static final String STRONG_PASSWORD = "Strong_Password_12345!";

    @MockBean
    private SecurityQuestionService securityQuestionService;

    @Autowired
    private UserValidator userValidator;

    @BeforeEach
    public void setup() {
        when(securityQuestionService.someDoNotExistWithIds(anySet())).thenReturn(false);
    }

    @Nested
    @DisplayName("validate")
    public class ValidateTests {
        @Test
        @DisplayName("validates that the create user request must not be null")
        void testNullCreateUserRequestThrowsException() {
            NullPointerException thrownException = assertThrows(NullPointerException.class, () -> userValidator.validate(null));
            assertEquals(NULL_CREATE_USER_REQUEST_ERROR_MESSAGE, thrownException.getMessage());
        }

        @ParameterizedTest(name = "validates that the password must not equal \"{0}\"")
        @ValueSource(strings = { "", " " })
        @NullSource
        void testThatBlankPasswordFails(String password) {
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email("test@" + UUID.randomUUID().toString())
                    .password(password)
                    .confirmationPassword(password)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "password must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the email must not equal \"{0}\"")
        @ValueSource(strings = {
                "",
                " ",
                "      "
        })
        @NullSource
        void testThatBlankEmailFails(String email) {
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(email)
                    .password(STRONG_PASSWORD)
                    .confirmationPassword(STRONG_PASSWORD)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "email must not be blank"
            );
        }

        @ParameterizedTest(name = "validates that the email must not equal \"{0}\"")
        @ValueSource(strings = {
                "plainaddress",
                "#@%^%#$@#$@#.com",
                "@example.com",
                "Joe Smith <email@example.com>",
                "email.example.com",
                "email@example@example.com",
                ".email@example.com",
                "email.@example.com",
                "email..email@example.com",
                "email@example.com (Joe Smith)",
                "email@-example.com",
                "email@example..com",
                "Abc..123@example.com",
        })
        void testThatInvalidEmailFormatFails(String email) throws InvalidUserException {
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(email)
                    .password(STRONG_PASSWORD)
                    .confirmationPassword(STRONG_PASSWORD)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "email must be a well-formed email address"
            );
        }

        @ParameterizedTest(name = "validates that the email can equal \"{0}\"")
        @ValueSource(strings = {
                "email@example.com",
                "firstname.lastname@example.com",
                "email@subdomain.example.com",
                "firstname+lastname@example.com",
                "email@123.123.123.123",
                "email@[123.123.123.123]",
                "\"email\"@example.com",
                "1234567890@example.com",
                "email@example-one.com",
                "_______@example.com",
                "email@example.name",
                "email@example.museum",
                "email@example.co.jp",
                "firstname-lastname@example.com"
        })
        void testThatValidEmailSucceeds(String email) {
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(email)
                    .password(STRONG_PASSWORD)
                    .confirmationPassword(STRONG_PASSWORD)
                    .securityQuestions(generateSecurityQuestions())
                    .build();
            assertDoesNotThrow(() -> {
                userValidator.validate(createUserRequest);
            });
        }

        @Test
        @DisplayName("validates that a password with 7 characters fails")
        void testThatPasswordWithLengthOfSevenCharactersFails() {
            String password = "aBcD1!2";
            assertEquals(7, password.length());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(null)
                    .password(password)
                    .confirmationPassword(password)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "password size must be between 8 and 71"
            );
        }

        @Test
        @DisplayName("validates that a password with 72 characters fails")
        void testThatPasswordWithLengthOfSeventyTwoCharactersFails() {
            String password = "aBcD1234!*".repeat(7) + "aB";
            assertEquals(72, password.length());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(null)
                    .password(password)
                    .confirmationPassword(password)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "password size must be between 8 and 71"
            );
        }

        @EmptySource
        @NullSource
        @ParameterizedTest(name = "validates that the security questions must not be {0}")
        void testEmptySecurityQuestions(List<CreateUserSecurityQuestionRequest> securityQuestions) {
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(securityQuestions)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "securityQuestions must not be empty"
            );
        }

        @Test
        @DisplayName("validates that the security question ids provided must all exist")
        void testValidExistenceOfSecurityQuestionIds() {
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestions();
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(true);
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE
            );
        }

        @Test
        @DisplayName("allows security question ids that exist")
        void allowsSecurityQuestionIdsThatExist() {
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestions();
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            InvalidException thrownException = assertThrows(InvalidException.class, () -> userValidator.validate(createUserRequest));
            assertFalse(thrownException.getErrors().contains(NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE));
        }

        @ValueSource(ints = { 1, 3, 4, 5, 10 })
        @ParameterizedTest(name = "does not allow {0} security questions to be created")
        void doesNotAllowNonTwoNumberOfSecurityQuestions(int numberOfSecurityQuestions) {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = generateSecurityQuestions(numberOfSecurityQuestions);
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(securityQuestionRequests)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    "securityQuestions size must be between 2 and 2"
            );
        }

        private List<CreateUserSecurityQuestionRequest> generateSecurityQuestions() {
            return generateSecurityQuestions(NUMBER_OF_ALLOWED_SECURITY_QUESTIONS);
        }

        private List<CreateUserSecurityQuestionRequest> generateSecurityQuestions(int size) {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = new ArrayList<>();
            for(int i = 0; i < size; i++) {
                securityQuestionRequests.add(CreateUserSecurityQuestionRequest.builder()
                        .answer(UUID.randomUUID().toString())
                        .id(Integer.toUnsignedLong(i))
                        .build());
            }
            return securityQuestionRequests;
        }
    }
}
