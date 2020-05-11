package com.tiernebre.tailgate.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RefreshTokenConverterTests {
    RefreshTokenConverter refreshTokenConverter = new RefreshTokenConverterImpl();

    @Nested
    public class ConvertToDtoTests {
        @Test
        @DisplayName("returns the converted DTO")
        public void returnsTheConvertedDto() {
            RefreshTokenEntity entity = RefreshTokenFactory.generateOneEntity();
            RefreshTokenDto dto = refreshTokenConverter.convertToDto(entity);
            assertAll(
                    () -> assertEquals(entity.getToken(), dto.getToken()),
                    () -> assertEquals(entity.getUserId(), dto.getUserId())
            );
        }
    }
}
