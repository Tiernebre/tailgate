package com.tiernebre.tailgate.security_questions;

import com.tiernebre.tailgate.jooq.tables.records.SecurityQuestionsRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenRecordPool;
import com.tiernebre.tailgate.user.UserRecordPool;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SecurityQuestionJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private SecurityQuestionRecordPool recordPool;

    @Autowired
    private SecurityQuestionJooqRepository securityQuestionJooqRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private PasswordResetTokenRecordPool passwordResetTokenRecordPool;

    @BeforeEach
    public void setup() {
        recordPool.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        passwordResetTokenRecordPool.deleteAll();
        userRecordPool.deleteAll();
        recordPool.deleteAll();
    }

    @Nested
    @DisplayName("getAll")
    public class GetAllTests {
        @Test
        @DisplayName("returns all of the security questions")
        public void returnsAllOfTheSecurityQuestions() {
            List<SecurityQuestionEntity> expectedQuestions = recordPool
                    .createMultiple()
                    .stream()
                    .map(securityQuestionsRecord -> securityQuestionsRecord.into(SecurityQuestionEntity.class))
                    .collect(Collectors.toList());;
            List<SecurityQuestionEntity> gottenQuestions = securityQuestionJooqRepository.getAll();
            assertNotNull(gottenQuestions);
            assertNotNull(gottenQuestions.get(0));
            assertNotNull(gottenQuestions.get(0).getId());
            assertNotNull(gottenQuestions.get(0).getQuestion());
            assertEquals(expectedQuestions, gottenQuestions);
        }

        @Test
        @DisplayName("returns an empty list if none exist")
        public void returnsAnEmptyListIfNoneExist() {
            List<SecurityQuestionEntity> gottenQuestions = securityQuestionJooqRepository.getAll();
            assertTrue(gottenQuestions.isEmpty());
        }
    }

    @Nested
    @DisplayName("allExistWithIds")
    public class AllExistWithIdsTests {
        @ParameterizedTest(name = "returns false if the set of ids provided is \"{0}\"")
        @NullAndEmptySource
        public void returnsFalseIfTheListOfIdsProvidedIsEmpty(Set<Long> ids) {
            assertFalse(securityQuestionJooqRepository.allExistWithIds(ids));
        }

        @Test
        @DisplayName("returns true if one id exists")
        public void returnsTrueIfOneIdExists() {
            Long id = recordPool.createOne().getId();
            assertTrue(securityQuestionJooqRepository.allExistWithIds(Collections.singleton(id)));
        }

        @Test
        @DisplayName("returns true if multiple ids exist")
        public void returnsTrueIfMultipleIdsExist() {
            Set<Long> ids = recordPool.createMultiple().stream().map(SecurityQuestionsRecord::getId).collect(Collectors.toSet());
            assertTrue(securityQuestionJooqRepository.allExistWithIds(ids));
        }

        @Test
        @DisplayName("returns false if one id does not exist")
        public void returnsFalseIfOneIdDoesNotExist() {
            assertFalse(securityQuestionJooqRepository.allExistWithIds(Collections.singleton(Long.MAX_VALUE)));
        }

        @Test
        @DisplayName("returns false if all ids do not exist")
        public void returnsFalseIfAllIdsDoNotExist() {
            Set<Long> ids = ImmutableSet.of(Long.MAX_VALUE, Long.MAX_VALUE - 1);
            assertFalse(securityQuestionJooqRepository.allExistWithIds(ids));
        }

        @Test
        @DisplayName("returns false if one of the provided ids does not exist, but another one of them does")
        public void returnsFalseIfOneOfTheProvidedIdsDoesNotExistButAnotherOneOfThemDoes() {
            Set<Long> ids = recordPool.createMultiple().stream().map(SecurityQuestionsRecord::getId).collect(Collectors.toSet());
            ids.add(Long.MAX_VALUE);
            assertFalse(securityQuestionJooqRepository.allExistWithIds(ids));
        }
    }

    @Nested
    @DisplayName("getAllForPasswordResetToken")
    class GetAllForPasswordResetToken {
        @Test
        @DisplayName("returns the security questions tied to a given password reset token")
        void returnsTheSecurityQuestionsTiedToAGivenPasswordResetToken() {
            UsersRecord usersRecord = userRecordPool.createAndSaveOneWithSecurityQuestions();
            List<SecurityQuestionsRecord> securityQuestions = recordPool.getSecurityQuestionsForUser(usersRecord);
            List<SecurityQuestionEntity> expectedSecurityQuestions = securityQuestions
                    .stream()
                    .map(securityQuestionsRecord -> securityQuestionsRecord.into(SecurityQuestionEntity.class))
                    .collect(Collectors.toList());
            String passwordResetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(usersRecord).getToken();
            List<SecurityQuestionEntity> foundSecurityQuestions = securityQuestionJooqRepository.getAllForPasswordResetToken(passwordResetToken);
            assertEquals(expectedSecurityQuestions, foundSecurityQuestions);
        }
    }
}
