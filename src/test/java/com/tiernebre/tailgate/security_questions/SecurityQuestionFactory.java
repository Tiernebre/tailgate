package com.tiernebre.tailgate.security_questions;

import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

public class SecurityQuestionFactory {
    public static SecurityQuestionDto generateOneDto() {
        return SecurityQuestionDto.builder()
                .id(new Random().nextLong())
                .question(generateQuestion())
                .build();
    }

    public static SecurityQuestionEntity generateOneEntity() {
        return SecurityQuestionEntity.builder()
                .id(new Random().nextLong())
                .question(generateQuestion())
                .build();
    }

    private static String generateQuestion() {
        return RandomStringUtils.randomAlphabetic(10) + "?";
    }
}
