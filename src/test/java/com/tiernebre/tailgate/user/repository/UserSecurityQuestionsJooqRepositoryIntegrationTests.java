package com.tiernebre.tailgate.user.repository;

import com.tiernebre.tailgate.jooq.tables.records.PasswordResetTokensRecord;
import com.tiernebre.tailgate.jooq.tables.records.UserSecurityQuestionsRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenConfigurationProperties;
import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenRecordPool;
import com.tiernebre.tailgate.user.UserRecordPool;
import org.apache.commons.collections4.MapUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class UserSecurityQuestionsJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private UserSecurityQuestionsJooqRepository userSecurityQuestionsJooqRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private PasswordResetTokenRecordPool passwordResetTokenRecordPool;

    @Autowired
    private PasswordResetTokenConfigurationProperties passwordResetTokenConfigurationProperties;

    @AfterEach
    public void cleanup() {
        passwordResetTokenRecordPool.deleteAll();
        userRecordPool.deleteAll();
    }

    @Nested
    @DisplayName("getAnswersForEmailAndResetToken")
    public class GetAnswersForEmailAndResetTokenTests {
        @Test
        @DisplayName("returns the security question answers if the email and reset token are legitimate")
        void returnsTheSecurityQuestionAnswersIfTheEmailAndResetTokenAreLegitimate() {
            UsersRecord user = userRecordPool.createAndSaveOneWithSecurityQuestions();
            Set<String> originalAnswers = userRecordPool
                    .getSecurityQuestionsForUserWithId(user.getId())
                    .stream()
                    .map(UserSecurityQuestionsRecord::getAnswer)
                    .collect(Collectors.toSet());
            String resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(user).getToken();
            Map<Long, String> foundAnswers = userSecurityQuestionsJooqRepository.getAnswersForEmailAndResetToken(user.getEmail(), resetToken);
            assertTrue(MapUtils.isNotEmpty(foundAnswers));
            assertTrue(originalAnswers.containsAll(foundAnswers.values()));
        }

        @Test
        @DisplayName("returns an empty set if the email is not legit")
        void returnsAnEmptySetIfTheEmailIsNotLegit() {
            UsersRecord user = userRecordPool.createAndSaveOneWithSecurityQuestions();
            String resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(user).getToken();
            Map<Long, String> answers = userSecurityQuestionsJooqRepository.getAnswersForEmailAndResetToken(UUID.randomUUID().toString(), resetToken);
            assertTrue(MapUtils.isEmpty(answers));
        }

        @Test
        @DisplayName("returns an empty set if the password reset token is not legit")
        void returnsAnEmptySetIfThePasswordResetTokenIsNotLegit() {
            UsersRecord user = userRecordPool.createAndSaveOneWithSecurityQuestions();
            Map<Long, String> answers = userSecurityQuestionsJooqRepository.getAnswersForEmailAndResetToken(user.getEmail(), UUID.randomUUID().toString());
            assertTrue(MapUtils.isEmpty(answers));
        }

        @Test
        @DisplayName("returns an empty set if the password reset token and email are not legit")
        void returnsAnEmptySetIfThePasswordResetTokenAndEmailAreNotLegit() {
            userRecordPool.createAndSaveOneWithSecurityQuestions();
            Map<Long, String> answers = userSecurityQuestionsJooqRepository.getAnswersForEmailAndResetToken(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            assertTrue(MapUtils.isEmpty(answers));
        }

        @Test
        @DisplayName("returns an empty set if the password reset token is expired")
        void returnsAnEmptySetIfThePasswordResetTokenIsExpired() {
            UsersRecord originalUser = userRecordPool.createAndSaveOneWithSecurityQuestions();
            PasswordResetTokensRecord resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(originalUser);
            resetToken.setCreatedAt(
                    LocalDateTime
                            .now()
                            .minusMinutes(passwordResetTokenConfigurationProperties.getExpirationWindowInMinutes())
                            .minusSeconds(1)
            );
            resetToken.store();
            Map<Long, String> answers = userSecurityQuestionsJooqRepository.getAnswersForEmailAndResetToken(originalUser.getEmail(), resetToken.getToken());
            assertTrue(MapUtils.isEmpty(answers));
        }
    }
}
