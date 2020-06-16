package com.tiernebre.tailgate.user.repository;

import com.tiernebre.tailgate.jooq.tables.records.PasswordResetTokensRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenConfigurationProperties;
import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenRecordPool;
import com.tiernebre.tailgate.user.UserRecordPool;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class UserPasswordJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private UserPasswordJooqRepository userPasswordJooqRepository;

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
    @DisplayName("updateOneWithEmailAndNonExpiredResetToken")
    public class UpdateOneWithEmailAndNonExpiredResetTokenTests {
        @Test
        @DisplayName("updates a password if the email and reset token provided exist.")
        void updatesAPasswordIfTheEmailAndResetTokenProvidedExist() {
            UsersRecord user = userRecordPool.createAndSaveOne();
            String resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(user).getToken();
            String password = UUID.randomUUID().toString();
            assertTrue(userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, user.getEmail(), resetToken));
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(user.getId(), user.getEmail());
            assertEquals(password, updatedUser.getPassword());
        }

        @Test
        @DisplayName("does not update multiple user passwords on accident")
        void doesNotUpdateMultipleUsersPasswordsOnAccident() {
            UsersRecord oneUser = userRecordPool.createAndSaveOne();
            UsersRecord anotherUser = userRecordPool.createAndSaveOne();
            String resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(oneUser).getToken();
            String password = UUID.randomUUID().toString();
            assertTrue(userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, oneUser.getEmail(), resetToken));
            UsersRecord updatedAnotherUser = userRecordPool.findOneByIdAndEmail(anotherUser.getId(), anotherUser.getEmail());
            assertEquals(anotherUser.getPassword(), updatedAnotherUser.getPassword());
        }

        @Test
        @DisplayName("does not update a password if the email does not exist, but the token does")
        void doesNotUpdateAPasswordIfTheEmailDoesNotExistButTheTokenDoes() {
            UsersRecord originalUser = userRecordPool.createAndSaveOne();
            String resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(originalUser).getToken();
            String password = UUID.randomUUID().toString();
            assertFalse(userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, UUID.randomUUID().toString(), resetToken));
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(originalUser.getId(), originalUser.getEmail());
            assertEquals(originalUser.getPassword(), updatedUser.getPassword());
        }

        @Test
        @DisplayName("does not update a password if the reset token does not exist, but the email does")
        void doesNotUpdateAPasswordIfTheResetTokenDoesNotExistButTheEmailDoes() {
            UsersRecord originalUser = userRecordPool.createAndSaveOne();
            String password = UUID.randomUUID().toString();
            assertFalse(userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, originalUser.getEmail(), UUID.randomUUID().toString()));
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(originalUser.getId(), originalUser.getEmail());
            assertEquals(originalUser.getPassword(), updatedUser.getPassword());
        }

        @Test
        @DisplayName("does not update a password if the reset token and email do not exist")
        void doesNotUpdateAPasswordIfTheResetTokenAndEmailDoNotExist() {
            UsersRecord originalUser = userRecordPool.createAndSaveOne();
            String password = UUID.randomUUID().toString();
            assertFalse(userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, UUID.randomUUID().toString(), UUID.randomUUID().toString()));
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(originalUser.getId(), originalUser.getEmail());
            assertEquals(originalUser.getPassword(), updatedUser.getPassword());
        }

        @Test
        @DisplayName("does not update a password if the reset token is expired")
        void doesNotUpdateAPasswordIfTheResetTokenIsExpired() {
            UsersRecord originalUser = userRecordPool.createAndSaveOne();
            PasswordResetTokensRecord resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(originalUser);
            resetToken.setCreatedAt(
                    LocalDateTime
                            .now()
                            .minusMinutes(passwordResetTokenConfigurationProperties.getExpirationWindowInMinutes())
                            .minusSeconds(1)
            );
            resetToken.store();
            String password = UUID.randomUUID().toString();
            assertFalse(userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, UUID.randomUUID().toString(), resetToken.getToken()));
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(originalUser.getId(), originalUser.getEmail());
            assertEquals(originalUser.getPassword(), updatedUser.getPassword());
        }
    }
    @Nested
    @DisplayName("getSecurityQuestionAnswersForEmailAndNonExpiredResetToken")
    public class GetSecurityQuestionAnswersForEmailAndNonExpiredResetTokenTests {
        @Test
        @DisplayName("returns the security question answers if the email and reset token are legitimate")
        void returnsTheSecurityQuestionAnswersIfTheEmailAndResetTokenAreLegitimate() {
            UsersRecord user = userRecordPool.createAndSaveOneWithSecurityQuestions();
            String resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(user).getToken();
            Set<String> answers = userPasswordJooqRepository.getSecurityQuestionAnswersForEmailAndNonExpiredResetToken(user.getEmail(), resetToken);
            assertTrue(CollectionUtils.isNotEmpty(answers));
        }

        @Test
        @DisplayName("returns an empty set if the email is not legit")
        void returnsAnEmptySetIfTheEmailIsNotLegit() {
            UsersRecord user = userRecordPool.createAndSaveOneWithSecurityQuestions();
            String resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(user).getToken();
            Set<String> answers = userPasswordJooqRepository.getSecurityQuestionAnswersForEmailAndNonExpiredResetToken(UUID.randomUUID().toString(), resetToken);
            assertTrue(CollectionUtils.isEmpty(answers));
        }

        @Test
        @DisplayName("returns an empty set if the password reset token is not legit")
        void returnsAnEmptySetIfThePasswordResetTokenIsNotLegit() {
            UsersRecord user = userRecordPool.createAndSaveOneWithSecurityQuestions();
            Set<String> answers = userPasswordJooqRepository.getSecurityQuestionAnswersForEmailAndNonExpiredResetToken(user.getEmail(), UUID.randomUUID().toString());
            assertTrue(CollectionUtils.isEmpty(answers));
        }

        @Test
        @DisplayName("returns an empty set if the password reset token and email are not legit")
        void returnsAnEmptySetIfThePasswordResetTokenAndEmailAreNotLegit() {
            userRecordPool.createAndSaveOneWithSecurityQuestions();
            Set<String> answers = userPasswordJooqRepository.getSecurityQuestionAnswersForEmailAndNonExpiredResetToken(UUID.randomUUID().toString(), UUID.randomUUID().toString());
            assertTrue(CollectionUtils.isEmpty(answers));
        }
    }
}
