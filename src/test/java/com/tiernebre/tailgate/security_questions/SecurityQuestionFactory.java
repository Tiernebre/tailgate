package com.tiernebre.tailgate.security_questions;

import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecurityQuestionFactory {
    public static SecurityQuestionDto generateOneDto() {
        return SecurityQuestionDto.builder()
                .id(new Random().nextLong())
                .question(generateQuestion())
                .build();
    }

    public static List<SecurityQuestionDto> generateMultipleDtos() {
        List<SecurityQuestionDto> securityQuestions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            securityQuestions.add(generateOneDto());
        }
        return securityQuestions;
    }

    public static List<SecurityQuestionEntity> generateManyEntities() {
        List<SecurityQuestionEntity> entities = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            entities.add(generateOneEntity());
        }
        return entities;
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
