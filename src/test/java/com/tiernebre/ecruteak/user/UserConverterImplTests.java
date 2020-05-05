package com.tiernebre.tailgate.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserConverterImplTests {
    @InjectMocks
    private UserConverterImpl userConverter;

    @Nested
    @DisplayName("convertFromCreateRequest")
    public class ConvertFromCreateRequestTests {
        @Test
        @DisplayName("converts a create request to an entity")
        public void testConvertFromCreateRequestProperlyMapsAnEntity() {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            UserEntity userEntity = userConverter.convertFromCreateRequest(createUserRequest);
            assertAll(
                    () -> assertEquals(createUserRequest.getPassword(), userEntity.getPassword()),
                    () -> assertEquals(createUserRequest.getConfirmationPassword(), userEntity.getPassword()),
                    () -> assertEquals(createUserRequest.getEmail(), userEntity.getEmail())
            );
        }
    }

    @Nested
    @DisplayName("convertFromDto")
    public class ConvertFromDtoTests {
        @DisplayName("converts a dto to an entity")
        @Test
        public void testConvertFromDtoProperlyMapsAnEntity() {
            UserDTO userDTO = UserFactory.generateOneDto();
            UserEntity userEntity = userConverter.convertFromDto(userDTO);
            assertAll(
                    () -> assertEquals(userDTO.getId(), userEntity.getId()),
                    () -> assertEquals(userDTO.getEmail(), userDTO.getEmail())
            );
        }
    }

    @Nested
    @DisplayName("convertFromEntity")
    public class ConvertFromEntityTests {
        @Test
        @DisplayName("converts an entity to a dto")
        public void testConvertFromDtoProperlyMapsAnEntity() {
            UserEntity userEntity = UserFactory.generateOneEntity();
            UserDTO userDTO = userConverter.convertFromEntity(userEntity);
            assertAll(
                    () -> assertEquals(userEntity.getId(), userDTO.getId()),
                    () -> assertEquals(userEntity.getEmail(), userDTO.getEmail())
            );
        }
    }
}
