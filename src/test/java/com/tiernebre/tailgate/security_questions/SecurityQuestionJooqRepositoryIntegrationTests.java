package com.tiernebre.tailgate.security_questions;

import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SecurityQuestionJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private SecurityQuestionRecordPool recordPool;

    @Autowired
    private SecurityQuestionJooqRepository securityQuestionJooqRepository;

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
    }
}
