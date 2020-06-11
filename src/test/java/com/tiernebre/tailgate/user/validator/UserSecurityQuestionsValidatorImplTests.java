package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.security_questions.SecurityQuestionService;
import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.CreateUserSecurityQuestionRequest;
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

import static com.tiernebre.tailgate.user.validator.UserValidationConstants.NUMBER_OF_ALLOWED_SECURITY_QUESTIONS;
import static com.tiernebre.tailgate.user.validator.UserValidationConstants.SAME_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE;
import static com.tiernebre.tailgate.user.validator.UserValidatorImpl.NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserSecurityQuestionsValidatorImplTests {
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
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestions();
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(true);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(errorsFound.contains(NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE));
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
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertFalse(errorsFound.contains(NON_EXISTENT_SECURITY_QUESTIONS_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("does not allow security questions with duplicate answers")
        void doesNotAllowSecurityQuestionsWithDuplicateAnswers() {
            String answer = "The same answer!";
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestions(answer);
            Set<String> securityQuestionAnswers = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getAnswer).collect(Collectors.toSet());
            assertEquals(1, securityQuestionAnswers.size());
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertTrue(errorsFound.contains(SAME_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE));
        }

        @Test
        @DisplayName("allows security questions with unique answers")
        void allowsSecurityQuestionsWithUniqueAnswers() {
            List<CreateUserSecurityQuestionRequest> createUserSecurityQuestionRequests = generateSecurityQuestions();
            Set<String> securityQuestionAnswers = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getAnswer).collect(Collectors.toSet());
            assertEquals(createUserSecurityQuestionRequests.size(), securityQuestionAnswers.size());
            Set<Long> securityQuestionIds = createUserSecurityQuestionRequests.stream().map(CreateUserSecurityQuestionRequest::getId).collect(Collectors.toSet());
            CreateUserRequest createUserRequest = CreateUserRequest.builder()
                    .securityQuestions(createUserSecurityQuestionRequests)
                    .build();
            when(securityQuestionService.someDoNotExistWithIds(eq(securityQuestionIds))).thenReturn(false);
            Set<String> errorsFound = userSecurityQuestionValidator.validate(createUserRequest);
            assertFalse(errorsFound.contains(SAME_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE));
        }

        private List<CreateUserSecurityQuestionRequest> generateSecurityQuestions() {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = new ArrayList<>();
            for(int i = 0; i < NUMBER_OF_ALLOWED_SECURITY_QUESTIONS; i++) {
                securityQuestionRequests.add(generateValidSecurityQuestion(Integer.toUnsignedLong(i), UUID.randomUUID().toString()));
            }
            return securityQuestionRequests;
        }

        private List<CreateUserSecurityQuestionRequest> generateSecurityQuestions(String answer) {
            List<CreateUserSecurityQuestionRequest> securityQuestionRequests = new ArrayList<>();
            for(int i = 0; i < NUMBER_OF_ALLOWED_SECURITY_QUESTIONS; i++) {
                securityQuestionRequests.add(generateValidSecurityQuestion(Integer.toUnsignedLong(i), answer));
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
