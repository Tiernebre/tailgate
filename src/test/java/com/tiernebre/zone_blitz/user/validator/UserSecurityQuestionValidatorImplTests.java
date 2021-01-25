package com.tiernebre.zone_blitz.user.validator;

import com.tiernebre.zone_blitz.security_questions.SecurityQuestionService;
import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.dto.CreateUserSecurityQuestionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

import static com.tiernebre.zone_blitz.user.validator.UserValidationConstants.*;
import static com.tiernebre.zone_blitz.user.validator.UserValidatorImpl.NON_EXISTENT_SECURITY_QUESTION_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserSecurityQuestionValidatorImplTests {
    @InjectMocks
    private UserSecurityQuestionValidatorImpl userSecurityQuestionValidator;

    @Mock
    private SecurityQuestionService securityQuestionService;

    @Mock
    private Validator validator;

    @BeforeEach
    public void setup() {
        when(validator.validate(any())).thenReturn(Collections.emptySet());
    }

    @Nested
    @DisplayName("validate")
    public class ValidateTests {
        @Test
        @DisplayName("validates that the security question ids provided must all exist")
        void testValidExistenceOfSecurityQuestionIds() {
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestion();
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(UUID.randomUUID().toString())
                    .password(UUID.randomUUID().toString())
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(true);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(errorsFound.contains(NON_EXISTENT_SECURITY_QUESTION_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("allows security question ids that exist")
        void allowsSecurityQuestionIdsThatExist() {
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestion();
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(UUID.randomUUID().toString())
                    .password(UUID.randomUUID().toString())
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertFalse(errorsFound.contains(NON_EXISTENT_SECURITY_QUESTION_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("does not allow security questions with duplicate answers")
        void doesNotAllowSecurityQuestionWithDuplicateAnswers() {
            String answer = "The same answer!";
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestion(answer);
            Set<String> securityQuestionAnswers = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getAnswer).collect(Collectors.toSet());
            assertEquals(1, securityQuestionAnswers.size());
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(UUID.randomUUID().toString())
                    .password(UUID.randomUUID().toString())
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(errorsFound.contains(SAME_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE));
        }

        @Test
        @DisplayName("does not allow security questions answers that contain the user password")
        void doesNotAllowSecurityQuestionAnswersThatContainTheUserPassword() {
            String password = UUID.randomUUID().toString();
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestion(password);
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .email(UUID.randomUUID().toString() + "@test.com")
                    .password(password)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(errorsFound.contains(SECURITY_QUESTION_ANSWERS_CANNOT_DUPLICATE_SENSITIVE_INFORMATION));
        }

        @Test
        @DisplayName("does not allow security questions answers that contain the user password case insensitively")
        void doesNotAllowSecurityQuestionAnswersThatContainTheUserPasswordCaseInsensitively() {
            String password = UUID.randomUUID().toString();
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestion(password.toUpperCase());
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .email(UUID.randomUUID().toString() + "@test.com")
                    .password(password.toLowerCase())
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(errorsFound.contains(SECURITY_QUESTION_ANSWERS_CANNOT_DUPLICATE_SENSITIVE_INFORMATION));
        }

        @Test
        @DisplayName("does not allow security questions answers that contain the user email")
        void doesNotAllowSecurityQuestionAnswersThatContainTheUserEmail() {
            String email = UUID.randomUUID().toString() + "@test.com";
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestion(email);
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .email(email)
                    .password(UUID.randomUUID().toString())
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(errorsFound.contains(SECURITY_QUESTION_ANSWERS_CANNOT_DUPLICATE_SENSITIVE_INFORMATION));
        }

        @Test
        @DisplayName("does not allow security questions answers that contain the user email case insensitively")
        void doesNotAllowSecurityQuestionAnswersThatContainTheUserEmailCaseInsensitively() {
            String email = UUID.randomUUID().toString() + "@test.com";
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestion(email.toLowerCase());
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .email(email.toUpperCase())
                    .password(UUID.randomUUID().toString())
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(errorsFound.contains(SECURITY_QUESTION_ANSWERS_CANNOT_DUPLICATE_SENSITIVE_INFORMATION));
        }

        @Test
        @DisplayName("allows security questions with unique answers")
        void allowsSecurityQuestionWithUniqueAnswers() {
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestion();
            Set<String> securityQuestionAnswers = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getAnswer).collect(Collectors.toSet());
            assertEquals(createUserSecurityQuestionRequests.size(), securityQuestionAnswers.size());
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(UUID.randomUUID().toString())
                    .password(UUID.randomUUID().toString())
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertFalse(errorsFound.contains(SAME_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE));
        }

        @Test
        @DisplayName("does not allow duplicate security question ids")
        void doesNotAllowDuplicateSecurityQuestionIds() {
            Long id = 1L;
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestion(id);
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests
                    .stream()
                    .map(CreateUserSecurityQuestionRequest::getId)
                    .collect(Collectors.toSet());
            assertNotEquals(createUserSecurityQuestionRequests.size(), securityQuestionIds.size());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .email(UUID.randomUUID().toString())
                    .password(UUID.randomUUID().toString())
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(errorsFound.contains(SAME_SECURITY_QUESTION_VALIDATION_MESSAGE));
        }

        private List<CreateUserSecurityQuestionRequest> generateSecurityQuestion() {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = new ArrayList<>();
            for(int i = 0; i < NUMBER_OF_ALLOWED_SECURITY_QUESTION; i++) {
                securityQuestionRequests.add(generateValidSecurityQuestion(Integer.toUnsignedLong(i), UUID.randomUUID().toString()));
            }
            return securityQuestionRequests;
        }

        private List<CreateUserSecurityQuestionRequest> generateSecurityQuestion(String answer) {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = new ArrayList<>();
            for(int i = 0; i < NUMBER_OF_ALLOWED_SECURITY_QUESTION; i++) {
                securityQuestionRequests.add(generateValidSecurityQuestion(Integer.toUnsignedLong(i), answer));
            }
            return securityQuestionRequests;
        }

        private List<CreateUserSecurityQuestionRequest> generateSecurityQuestion(Long id) {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = new ArrayList<>();
            for(int i = 0; i < NUMBER_OF_ALLOWED_SECURITY_QUESTION; i++) {
                securityQuestionRequests.add(generateValidSecurityQuestion(id, UUID.randomUUID().toString()));
            }
            return securityQuestionRequests;
        }

        private CreateUserSecurityQuestionRequest generateValidSecurityQuestion(Long id, String answer) {
            return CreateUserSecurityQuestionRequest.builder()
                    .answer(answer)
                    .id(id)
                    .build();
        }
    }
}
