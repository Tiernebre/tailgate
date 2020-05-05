package com.tiernebre.tailgate.user;

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

    public static UserDTO generateOneDto() {
        return UserDTO.builder()
                .id(1L)
                .email(generateEmail())
                .build();
    }

    private static String generateEmail() {
        return String.format("test-user-%s@test.com", UUID.randomUUID().toString());
    }
}
