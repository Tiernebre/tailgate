package com.tiernebre.tailgate.security_questions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

import java.util.List;

import static org.junit.Assert.assertEquals;
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
}
