package com.tiernebre.zone_blitz.user.validator;

import com.tiernebre.zone_blitz.exception.InvalidException;
import com.tiernebre.zone_blitz.test.AbstractIntegrationTestingSuite;
import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.dto.CreateUserSecurityQuestionRequest;
import com.tiernebre.zone_blitz.user.exception.InvalidUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static com.tiernebre.zone_blitz.test.ValidatorTestUtils.assertThatValidationInvalidatedCorrectly;
import static com.tiernebre.zone_blitz.user.validator.UserValidationConstants.*;
import static com.tiernebre.zone_blitz.user.validator.UserValidatorImpl.NULL_CREATE_USER_REQUEST_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserValidatorImplIntegrationTests extends AbstractIntegrationTestingSuite {
    private static final String STRONG_PASSWORD = "Strong_Password_12345!";

    @Autowired
    private UserValidator userValidator;

    @MockBean
    private UserSecurityQuestionValidator securityQuestionValidator;

    @BeforeEach
    public void setup() {
        when(securityQuestionValidator.validate(any())).thenReturn(Collections.emptySet());
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
        @ValueSource(strings = { " " })
        @EmptySource
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
        @ArgumentsSource(InvalidEmailArgumentsProvider.class)
        void testThatInvalidEmailFormatFails(String email) {
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
        @ArgumentsSource(ValidEmailArgumentsProvider.class)
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
                    NUMBER_OF_SECURITY_QUESTIONS_VALIDATION_MESSAGE
            );
        }

        @ValueSource(ints = { 2 })
        @ParameterizedTest(name = "allows {0} security questions to be created")
        void allowsNumberOfSecurityQuestions(int numberOfSecurityQuestions) {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = generateSecurityQuestions(numberOfSecurityQuestions);
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(securityQuestionRequests)
                    .build();
            InvalidException thrownException = assertThrows(InvalidException.class, () -> userValidator.validate(createUserRequest));
            assertFalse(thrownException.getErrors().contains("securityQuestions " + NUMBER_OF_SECURITY_QUESTIONS_VALIDATION_MESSAGE));
        }

        @Test
        @DisplayName("does not allow null security questions")
        void doesNotAllowNullSecurityQuestions() {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_ALLOWED_SECURITY_QUESTIONS; i++) {
                securityQuestionRequests.add(null);
            }
            assertTrue(securityQuestionRequests.stream().allMatch(Objects::isNull));
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(securityQuestionRequests)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    NULL_SECURITY_QUESTION_ENTRIES_VALIDATION_MESSAGE
            );
        }

        @Test
        @DisplayName("does not allow partial null security questions")
        void doesNotAllowPartialNullSecurityQuestions() {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_ALLOWED_SECURITY_QUESTIONS; i++) {
                if (i % 2 == 0) {
                    securityQuestionRequests.add(null);
                } else {
                    securityQuestionRequests.add(generateValidSecurityQuestion());
                }
            }
            assertTrue(securityQuestionRequests.stream().anyMatch(Objects::isNull));
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(securityQuestionRequests)
                    .build();
            assertThatValidationInvalidatedCorrectly(
                    userValidator,
                    createUserRequest,
                    InvalidUserException.class,
                    NULL_SECURITY_QUESTION_ENTRIES_VALIDATION_MESSAGE
            );
        }

        private List<CreateUserSecurityQuestionRequest> generateSecurityQuestions() {
            return generateSecurityQuestions(NUMBER_OF_ALLOWED_SECURITY_QUESTIONS);
        }

        private List<CreateUserSecurityQuestionRequest> generateSecurityQuestions(int size) {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = new ArrayList<>();
            for(int i = 0; i < size; i++) {
                securityQuestionRequests.add(generateValidSecurityQuestion(Integer.toUnsignedLong(i)));
            }
            return securityQuestionRequests;
        }

        private CreateUserSecurityQuestionRequest generateValidSecurityQuestion() {
            return generateValidSecurityQuestion(new Random().nextLong());
        }

        private CreateUserSecurityQuestionRequest generateValidSecurityQuestion(Long id) {
            return CreateUserSecurityQuestionRequest.builder()
                    .answer(UUID.randomUUID().toString())
                    .id(id)
                    .build();
        }
    }
}
