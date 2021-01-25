package com.tiernebre.zone_blitz.user.service;


import com.tiernebre.zone_blitz.jooq.tables.records.SecurityQuestionRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserSecurityQuestionRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserRecord;
import com.tiernebre.zone_blitz.security_questions.SecurityQuestionRecordPool;
import com.tiernebre.zone_blitz.test.AbstractIntegrationTestingSuite;
import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.dto.CreateUserSecurityQuestionRequest;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.exception.InvalidUserException;
import com.tiernebre.zone_blitz.user.exception.UserAlreadyExistsException;
import com.tiernebre.zone_blitz.user.validator.UserValidator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@MockBeans({
        @MockBean(UserConfirmationService.class)
})
public class UserServiceImplIntegrationTests extends AbstractIntegrationTestingSuite {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private SecurityQuestionRecordPool securityQuestionRecordPool;

    @MockBean
    private UserValidator validator;

    @BeforeEach
    public void setup() throws InvalidUserException {
        doNothing().when(validator).validate(any());
    }

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("fully persists a user with correct information")
        public void testCreateOneFullyPersists() throws InvalidUserException, UserAlreadyExistsException {
            CreateUserRequest createUserRequest = generateValidUserRequest();
            Set<String> expectedSecurityQuestionAnswers = createUserRequest
                    .getSecurityQuestions()
                    .stream()
                    .map(CreateUserSecurityQuestionRequest::getAnswer)
                    .collect(Collectors.toSet());
            UserDto createdUser = userService.createOne(createUserRequest);
            UserRecord userFound = userRecordPool.findOneByIdAndEmail(createdUser.getId(), createdUser.getEmail());
            Set<String> gottenSecurityQuestionAnswers = userRecordPool
                    .getSecurityQuestionForUserWithId(userFound.getId())
                    .stream()
                    .map(UserSecurityQuestionRecord::getAnswer)
                    .collect(Collectors.toSet());
            Set<String> intersectionOfSecurityQuestionAnswers = new HashSet<>(expectedSecurityQuestionAnswers);
            intersectionOfSecurityQuestionAnswers.retainAll(gottenSecurityQuestionAnswers);
            assertAll(
                () -> assertNotEquals(createUserRequest.getPassword(), userFound.getPassword()),
                () -> assertEquals(createUserRequest.getEmail(), userFound.getEmail()),
                () -> assertTrue(intersectionOfSecurityQuestionAnswers.isEmpty())
            );
        }
    }

    @Nested
    @DisplayName("findOneByEmailAndPassword")
    public class FindOneByEmailAndPasswordTests {
        @Test
        @DisplayName("fully retrieves a user by email and password that is legitimate")
        public void testCreateOneFullyPersists() throws InvalidUserException, UserAlreadyExistsException {
            CreateUserRequest createUserRequest = generateValidUserRequest();
            UserDto createdUser = userService.createOne(createUserRequest);
            UserDto foundUser = userService.findOneByEmailAndPassword(createUserRequest.getEmail(), createUserRequest.getPassword()).orElse(null);
            assertNotNull(foundUser);
            assertAll(
                    () -> assertEquals(createdUser.getId(), foundUser.getId()),
                    () -> assertEquals(createUserRequest.getEmail(), foundUser.getEmail())
            );
        }

        @Test
        @DisplayName("returns empty if a user email is correct but the password is not")
        public void testCreateOneEnforcesPasswordMatching() throws InvalidUserException, UserAlreadyExistsException {
            CreateUserRequest createUserRequest = generateValidUserRequest();
            userService.createOne(createUserRequest);
            Optional<UserDto> foundUser = userService.findOneByEmailAndPassword(createUserRequest.getEmail(), createUserRequest.getPassword() + UUID.randomUUID().toString());
            assertTrue(foundUser.isEmpty());
        }
    }

    private CreateUserRequest generateValidUserRequest() {
        List<SecurityQuestionRecord> securityQuestionsCreated = securityQuestionRecordPool.createMultiple();
        Set<Long> securityQuestionIds = securityQuestionsCreated
                .stream()
                .map(SecurityQuestionRecord::getId)
                .collect(Collectors.toSet());
        return UserFactory.generateOneCreateUserRequest(securityQuestionIds);
    }
}
