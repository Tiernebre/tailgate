package com.tiernebre.tailgate.security_questions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityQuestionServiceImplTests {
    @Mock
    private SecurityQuestionRepository securityQuestionRepository;

    @Mock
    private SecurityQuestionConverter securityQuestionConverter;

    @InjectMocks
    private SecurityQuestionServiceImpl securityQuestionService;

    @Nested
    @DisplayName("getAll")
    public class GetAllTests {
        @Test
        @DisplayName("returns the gotten security questions")
        public void returnsTheGottenSecurityQuestions() {
            List<SecurityQuestionEntity> entities = ImmutableList.of(
                    SecurityQuestionFactory.generateOneEntity(),
                    SecurityQuestionFactory.generateOneEntity()
            );
            List<SecurityQuestionDto> expectedDtos = ImmutableList.of(
                    SecurityQuestionFactory.generateOneDto(),
                    SecurityQuestionFactory.generateOneDto()
            );
            when(securityQuestionRepository.getAll()).thenReturn(entities);
            when(securityQuestionConverter.createFromEntities(eq(entities))).thenReturn(expectedDtos);
            List<SecurityQuestionDto> gottenSecurityQuestions = securityQuestionService.getAll();
            assertEquals(expectedDtos, gottenSecurityQuestions);
        }
    }

    @Nested
    @DisplayName("allExistWithIds")
    public class AllExistWithIdsTests {
        @Test
        @DisplayName("returns the result from the repository")
        public void returnsTheResultFromTheRepository() {
            Set<Long> ids = ImmutableSet.of(1L, 5L, 12L);
            when(securityQuestionRepository.allExistWithIds(eq(ids))).thenReturn(true);
            assertTrue(securityQuestionService.allExistWithIds(ids));
        }
    }
}
