package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.entity.UserEntity;

import java.util.UUID;

/**
 * Generator of easy-to-utilize mock test data for User related data POJOs.
 */
public class UserFactory {
    public static UserEntity generateOneEntity() {
        return generateOneEntity(null);
    }

    public static UserEntity generateOneEntity(Long id) {
        return UserEntity.builder()
                .id(id)
                .email(generateEmail())
                .password(UUID.randomUUID().toString())
                .build();
    }

    public static CreateUserRequest generateOneCreateUserRequest() {
        String password = "Strong_Password_12345!";
        return CreateUserRequest.builder()
                .email(generateEmail())
                .password(password)
                .confirmationPassword(password)
                .build();
    }

    public static UserDto generateOneDto() {
        return UserDto.builder()
                .id(1L)
                .email(generateEmail())
                .build();
    }

    private static String generateEmail() {
        return String.format("test-user-%s@test.com", UUID.randomUUID().toString());
    }
}
