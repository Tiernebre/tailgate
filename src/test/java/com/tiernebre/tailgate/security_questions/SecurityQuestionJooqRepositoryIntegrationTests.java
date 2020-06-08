package com.tiernebre.tailgate.security_questions;

import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class SecurityQuestionJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private SecurityQuestionRecordPool recordPool;

    @Autowired
    private SecurityQuestionJooqRepository securityQuestionJooqRepository;

    @BeforeEach
    public void setup() {
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
        @ParameterizedTest(name = "returns false if the set of ids provided is empty")
        @NullAndEmptySource
        public void returnsFalseIfTheListOfIdsProvidedIsEmpty(Set<Long> ids) {
            assertFalse(securityQuestionJooqRepository.allExistWithIds(ids));
        }
    }
}
