package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.CreateUserSecurityQuestionRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.entity.UserEntity;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
        return generateOneCreateUserRequest(generateSecurityQuestionIds());
    }

    public static CreateUserRequest generateOneCreateUserRequest(Set<Long> securityQuestionIds) {
        String password = "Strong_Password_12345!";
        return CreateUserRequest.builder()
                .email(generateEmail())
                .password(password)
                .confirmationPassword(password)
                .securityQuestions(generateSecurityQuestionRequests(securityQuestionIds))
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

    private static List<CreateUserSecurityQuestionRequest> generateSecurityQuestionRequests() {
        return generateSecurityQuestionRequests(generateSecurityQuestionIds());
    }

    private static Set<Long> generateSecurityQuestionIds() {
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 2; i++) {
            ids.add(Integer.toUnsignedLong(i));
        }
        return ids;
    }

    private static List<CreateUserSecurityQuestionRequest> generateSecurityQuestionRequests(Set<Long> ids) {
        return ids.stream().map(UserFactory::generateSecurityQuestionRequest).collect(Collectors.toList());
    }

    private static CreateUserSecurityQuestionRequest generateSecurityQuestionRequest(Long id) {
        return CreateUserSecurityQuestionRequest.builder()
                .id(id)
                .answer(RandomStringUtils.randomAlphabetic(20))
                .build();
    }
}
