package com.tiernebre.zone_blitz.user.repository;

import com.tiernebre.zone_blitz.jooq.tables.records.PasswordResetTokenRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserSecurityQuestionRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserRecord;
import com.tiernebre.zone_blitz.test.AbstractIntegrationTestingSuite;
import com.tiernebre.zone_blitz.token.password_reset.PasswordResetTokenConfigurationProperties;
import com.tiernebre.zone_blitz.token.password_reset.PasswordResetTokenRecordPool;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import org.apache.commons.collections4.MapUtils;
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

public class UserSecurityQuestionJooqRepositoryIntegrationTests extends AbstractIntegrationTestingSuite {
    @Autowired
    private UserSecurityQuestionJooqRepository userSecurityQuestionJooqRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private PasswordResetTokenRecordPool passwordResetTokenRecordPool;

    @Autowired
    private PasswordResetTokenConfigurationProperties passwordResetTokenConfigurationProperties;

    @Nested
    @DisplayName("getAnswersForEmailAndResetToken")
    public class GetAnswersForEmailAndResetTokenTests {
        @Test
        @DisplayName("returns the security question answers if the email and reset token are legitimate")
        void returnsTheSecurityQuestionAnswersIfTheEmailAndResetTokenAreLegitimate() {
            UserRecord user = userRecordPool.createAndSaveOneWithSecurityQuestion();
            Set<String> originalAnswers = userRecordPool
                    .getSecurityQuestionForUserWithId(user.getId())
                    .stream()
                    .map(UserSecurityQuestionRecord::getAnswer)
                    .collect(Collectors.toSet());
            UUID resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(user).getToken();
            Map<Long, String> foundAnswers = userSecurityQuestionJooqRepository.getAnswersForEmailAndResetToken(user.getEmail(), resetToken);
            assertTrue(MapUtils.isNotEmpty(foundAnswers));
            assertTrue(originalAnswers.containsAll(foundAnswers.values()));
        }

        @Test
        @DisplayName("returns an empty set if the email is not legit")
        void returnsAnEmptySetIfTheEmailIsNotLegit() {
            UserRecord user = userRecordPool.createAndSaveOneWithSecurityQuestion();
            UUID resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(user).getToken();
            Map<Long, String> answers = userSecurityQuestionJooqRepository.getAnswersForEmailAndResetToken(UUID.randomUUID().toString(), resetToken);
            assertTrue(MapUtils.isEmpty(answers));
        }

        @Test
        @DisplayName("returns an empty set if the user has no password reset token tied to them")
        void returnsAnEmptySetIfTheUserHasNoPasswordResetTokenTiedToThem() {
            UserRecord user = userRecordPool.createAndSaveOneWithSecurityQuestion();
            Map<Long, String> answers = userSecurityQuestionJooqRepository.getAnswersForEmailAndResetToken(user.getEmail(), UUID.randomUUID());
            assertTrue(MapUtils.isEmpty(answers));
        }

        @Test
        @DisplayName("returns an empty set if the password reset token is not legit")
        void returnsAnEmptySetIfThePasswordResetTokenIsNotLegit() {
            UserRecord user = userRecordPool.createAndSaveOneWithSecurityQuestion();
            passwordResetTokenRecordPool.createAndSaveOneForUser(user);
            Map<Long, String> answers = userSecurityQuestionJooqRepository.getAnswersForEmailAndResetToken(user.getEmail(), UUID.randomUUID());
            assertTrue(MapUtils.isEmpty(answers));
        }

        @Test
        @DisplayName("returns an empty set if the password reset token and email are not legit")
        void returnsAnEmptySetIfThePasswordResetTokenAndEmailAreNotLegit() {
            UserRecord user = userRecordPool.createAndSaveOneWithSecurityQuestion();
            passwordResetTokenRecordPool.createAndSaveOneForUser(user);
            Map<Long, String> answers = userSecurityQuestionJooqRepository.getAnswersForEmailAndResetToken(UUID.randomUUID().toString(), UUID.randomUUID());
            assertTrue(MapUtils.isEmpty(answers));
        }

        @Test
        @DisplayName("returns an empty set if the password reset token is expired")
        void returnsAnEmptySetIfThePasswordResetTokenIsExpired() {
            UserRecord originalUser = userRecordPool.createAndSaveOneWithSecurityQuestion();
            PasswordResetTokenRecord resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(originalUser);
            resetToken.setCreatedAt(
                    LocalDateTime
                            .now()
                            .minusMinutes(passwordResetTokenConfigurationProperties.getExpirationWindowInMinutes())
                            .minusSeconds(1)
            );
            resetToken.store();
            Map<Long, String> answers = userSecurityQuestionJooqRepository.getAnswersForEmailAndResetToken(originalUser.getEmail(), resetToken.getToken());
            assertTrue(MapUtils.isEmpty(answers));
        }
    }
}
