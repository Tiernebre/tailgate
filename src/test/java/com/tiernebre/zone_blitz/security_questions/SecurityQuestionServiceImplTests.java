package com.tiernebre.zone_blitz.security_questions;

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
import java.util.UUID;

import static org.junit.Assert.*;
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
    @DisplayName("someDoNotExistWithIdsTests")
    public class SomeDoNotExistWithIdsTests {
        @Test
        @DisplayName("returns the opposite result of whether all ids exist (true case)")
        public void returnsTheOppositeResultOfWhetherAllIdsExistTrueCase() {
            Set<Long> ids = ImmutableSet.of(1L, 5L, 12L);
            when(securityQuestionRepository.allExistWithIds(eq(ids))).thenReturn(true);
            assertFalse(securityQuestionService.someDoNotExistWithIds(ids));
        }

        @Test
        @DisplayName("returns the opposite result of whether all ids exist (false case)")
        public void returnsTheOppositeResultOfWhetherAllIdsExistFalseCase() {
            Set<Long> ids = ImmutableSet.of(1L, 5L, 12L);
            when(securityQuestionRepository.allExistWithIds(eq(ids))).thenReturn(false);
            assertTrue(securityQuestionService.someDoNotExistWithIds(ids));
        }
    }

    @Nested
    @DisplayName("getAllForPasswordResetToken")
    public class GetAllForPasswordResetToken {
        @Test
        @DisplayName("returns the found security questions from a given password reset token converted to DTOs")
        public void returnsTheFoundSecurityQuestionsFromAGivenPasswordResetTokenConvertedToDtos() {
            List<SecurityQuestionEntity> entities = SecurityQuestionFactory.generateManyEntities();
            UUID passwordResetToken = UUID.randomUUID();
            List<SecurityQuestionDto> expectedDtos = SecurityQuestionFactory.generateMultipleDtos();
            when(securityQuestionRepository.getAllForPasswordResetToken(eq(passwordResetToken))).thenReturn(entities);
            when(securityQuestionConverter.createFromEntities(eq(entities))).thenReturn(expectedDtos);
            List<SecurityQuestionDto> foundDtos = securityQuestionService.getAllForPasswordResetToken(passwordResetToken);
            assertEquals(expectedDtos, foundDtos);
        }
    }
}
