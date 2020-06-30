package com.tiernebre.zone_blitz.security_questions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class SecurityQuestionConverterImplTests {
    @InjectMocks
    private SecurityQuestionConverterImpl securityQuestionConverter;

    @Nested
    @DisplayName("convertFromDto")
    public class ConvertFromDtoTests {
        @Test
        @DisplayName("returns the converted entity from the given DTO")
        public void returnsTheConvertedEntityFromTheGivenDto () {
            SecurityQuestionDto dto = SecurityQuestionFactory.generateOneDto();
            SecurityQuestionEntity entity = securityQuestionConverter.convertFromDto(dto);
            assertAll(
                    () -> assertEquals(dto.getId(), entity.getId()),
                    () -> assertEquals(dto.getQuestion(), entity.getQuestion())
            );
        }
    }

    @Nested
    @DisplayName("convertFromEntity")
    public class ConvertFromEntityTests {
        @Test
        @DisplayName("returns the converted dto from the given entity")
        public void returnsTheConvertedEntityFromTheGivenDto () {
            SecurityQuestionEntity entity = SecurityQuestionFactory.generateOneEntity();
            SecurityQuestionDto dto = securityQuestionConverter.convertFromEntity(entity);
            assertAll(
                    () -> assertEquals(entity.getId(), dto.getId()),
                    () -> assertEquals(entity.getQuestion(), dto.getQuestion())
            );
        }
    }
}
