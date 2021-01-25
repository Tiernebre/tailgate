package com.tiernebre.zone_blitz.security_questions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityQuestionRestfulControllerTests {
    @InjectMocks
    private SecurityQuestionRestfulController securityQuestionRestfulController;

    @Mock
    private SecurityQuestionService securityQuestionService;

    @Nested
    @DisplayName("getAll")
    public class GetAllTests {
        @Test
        public void returnsTheGottenSecurityQuestion() {
            List<SecurityQuestionDto> expectedQuestions = SecurityQuestionFactory.generateMultipleDtos();
            when(securityQuestionService.getAll()).thenReturn(expectedQuestions);
            List<SecurityQuestionDto> gottenQuestions = securityQuestionRestfulController.getAll();
            assertEquals(expectedQuestions, gottenQuestions);
        }
    }
}
