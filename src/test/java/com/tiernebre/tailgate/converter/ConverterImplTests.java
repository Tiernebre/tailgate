package com.tiernebre.tailgate.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.Assert.*;
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

    @Nested
    @DisplayName("createFromDtos")
    public class CreateFromDtosTests {
        @Test
        @DisplayName("returns a converted list of entities from dto objects")
        public void returnsAConvertedListOfEntitiesFromDtoObjects() {
            List<StubDto> dtos = ImmutableList.of(
                    StubDto.builder().id(1).name(UUID.randomUUID().toString()).build(),
                    StubDto.builder().id(2).name(UUID.randomUUID().toString()).build()
            );
            List<StubEntity> expectedEntities = ImmutableList.of(
                    StubEntity.builder().id(1).name(UUID.randomUUID().toString()).build(),
                    StubEntity.builder().id(2).name(UUID.randomUUID().toString()).build()
            );
            for (int i = 0; i < dtos.size(); i++) {
                StubDto dto = dtos.get(i);
                StubEntity expectedEntity = expectedEntities.get(i);
                when(fromDto.apply(eq(dto))).thenReturn(expectedEntity);
            }
            List<StubEntity> returnedEntities = stubConverter.createFromDtos(dtos);
            assertEquals(expectedEntities,returnedEntities);
        }

        @Test
        @DisplayName("returns null if given a null value")
        public void returnsNullIfGivenANullValue() {
            assertNull(stubConverter.createFromDtos(null));
        }
    }

    @Nested
    @DisplayName("createFromEntities")
    public class CreateFromEntitiesTests {
        @Test
        @DisplayName("returns a converted list of dtos from entity objects")
        public void returnsAConvertedListOfDtosFromEntityObjects() {
            List<StubEntity> entities = ImmutableList.of(
                    StubEntity.builder().id(1).name(UUID.randomUUID().toString()).build(),
                    StubEntity.builder().id(2).name(UUID.randomUUID().toString()).build()
            );
            List<StubDto> expectedDtos = ImmutableList.of(
                    StubDto.builder().id(1).name(UUID.randomUUID().toString()).build(),
                    StubDto.builder().id(2).name(UUID.randomUUID().toString()).build()
            );
            for (int i = 0; i < entities.size(); i++) {
                StubEntity entity = entities.get(i);
                StubDto expectedDto = expectedDtos.get(i);
                when(fromEntity.apply(eq(entity))).thenReturn(expectedDto);
            }
            List<StubDto> returnedDtos = stubConverter.createFromEntities(entities);
            assertEquals(expectedDtos, returnedDtos);
        }

        @Test
        @DisplayName("returns null if given a null value")
        public void returnsNullIfGivenANullValue() {
            assertNull(stubConverter.createFromEntities(null));
        }
    }
}
