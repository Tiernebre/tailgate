package com.tiernebre.tailgate.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConverterImplTests {
    private StubConverter stubConverter;

    @Mock
    private Function<StubDto, StubEntity> fromDto;

    @Mock
    private Function<StubEntity, StubDto> fromEntity;

    @BeforeEach
    public void setup() {
        stubConverter = new StubConverter(
                fromDto,
                fromEntity
        );
    }

    @Nested
    @DisplayName("convertFromDto")
    public class ConvertFromDtoTests {
        @Test
        @DisplayName("returns the applied fromDto function result")
        public void returnsTheAppliedFromDtoFunctionResult() {
            StubDto stubDto = StubDto.builder().build();
            StubEntity expectedEntity = StubEntity.builder()
                    .id(new Random().nextLong())
                    .build();
            when(fromDto.apply(eq(stubDto))).thenReturn(expectedEntity);
            StubEntity returnedEntity = stubConverter.convertFromDto(stubDto);
            assertEquals(expectedEntity, returnedEntity);
        }

        @Test
        @DisplayName("returns null if given a null value")
        public void returnsNullIfGivenANullValue() {
            assertNull(stubConverter.convertFromDto(null));
        }
    }

    @Nested
    @DisplayName("convertFromEntity")
    public class ConvertFromEntityTests {
        @Test
        @DisplayName("returns the applied fromEntity function result")
        public void returnsTheAppliedFromDtoFunctionResult() {
            StubEntity stubEntity = StubEntity.builder().build();
            StubDto expectedDto = StubDto.builder()
                    .id(new Random().nextLong())
                    .build();
            when(fromEntity.apply(eq(stubEntity))).thenReturn(expectedDto);
            StubDto returnedDto = stubConverter.convertFromEntity(stubEntity);
            assertEquals(expectedDto, returnedDto);
        }

        @Test
        @DisplayName("returns null if given a null value")
        public void returnsNullIfGivenANullValue() {
           assertNull(stubConverter.convertFromEntity(null));
        }
    }
}
