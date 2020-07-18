package com.tiernebre.zone_blitz.user.repository;

import com.tiernebre.zone_blitz.jooq.tables.records.PasswordResetTokensRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UsersRecord;
import com.tiernebre.zone_blitz.test.DatabaseIntegrationTestSuite;
import com.tiernebre.zone_blitz.token.password_reset.PasswordResetTokenConfigurationProperties;
import com.tiernebre.zone_blitz.token.password_reset.PasswordResetTokenRecordPool;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;
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

    @Nested
    @DisplayName("updateOneWithEmailAndNonExpiredResetToken")
    public class UpdateOneWithEmailAndNonExpiredResetTokenTests {
        @Test
        @DisplayName("updates a password if the email and reset token provided exist.")
        void updatesAPasswordIfTheEmailAndResetTokenProvidedExist() {
            UsersRecord user = userRecordPool.createAndSaveOne();
            UUID resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(user).getToken();
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
            UUID resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(oneUser).getToken();
            String password = UUID.randomUUID().toString();
            assertTrue(userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, oneUser.getEmail(), resetToken));
            UsersRecord updatedAnotherUser = userRecordPool.findOneByIdAndEmail(anotherUser.getId(), anotherUser.getEmail());
            assertEquals(anotherUser.getPassword(), updatedAnotherUser.getPassword());
        }

        @Test
        @DisplayName("does not update a password if the email does not exist, but the token does")
        void doesNotUpdateAPasswordIfTheEmailDoesNotExistButTheTokenDoes() {
            UsersRecord originalUser = userRecordPool.createAndSaveOne();
            UUID resetToken = passwordResetTokenRecordPool.createAndSaveOneForUser(originalUser).getToken();
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
            assertFalse(userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, originalUser.getEmail(), UUID.randomUUID()));
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(originalUser.getId(), originalUser.getEmail());
            assertEquals(originalUser.getPassword(), updatedUser.getPassword());
        }

        @Test
        @DisplayName("does not update a password if the reset token and email do not exist")
        void doesNotUpdateAPasswordIfTheResetTokenAndEmailDoNotExist() {
            UsersRecord originalUser = userRecordPool.createAndSaveOne();
            String password = UUID.randomUUID().toString();
            assertFalse(userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, UUID.randomUUID().toString(), UUID.randomUUID()));
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
            assertFalse(userPasswordJooqRepository.updateOneWithEmailAndNonExpiredResetToken(password, originalUser.getEmail(), resetToken.getToken()));
            UsersRecord updatedUser = userRecordPool.findOneByIdAndEmail(originalUser.getId(), originalUser.getEmail());
            assertEquals(originalUser.getPassword(), updatedUser.getPassword());
        }
    }

    @Nested
    @DisplayName("updateOneForId")
    public class UpdateOneForId {
        @Test
        @DisplayName("updates a password for a provided id")
        void updatesAPasswordForAProvidedId() {
            UsersRecord user = userRecordPool.createAndSaveOne();
            String newPassword = UUID.randomUUID().toString();
            userPasswordJooqRepository.updateOneForId(user.getId(), newPassword);
            user.refresh();
            assertEquals(newPassword, user.getPassword());
        }

        @Test
        @DisplayName("returns true if a password was updated")
        void returnsTrueIfAPasswordWasUpdated() {
            UsersRecord user = userRecordPool.createAndSaveOne();
            String newPassword = UUID.randomUUID().toString();
            assertTrue(userPasswordJooqRepository.updateOneForId(user.getId(), newPassword));
        }

        @Test
        @DisplayName("returns false if a password was not updated")
        void returnsFalseIfAPasswordWasNotUpdated() {
            assertFalse(userPasswordJooqRepository.updateOneForId(Long.MAX_VALUE, UUID.randomUUID().toString()));
        }
    }

    @Nested
    @DisplayName("findOneForId")
    public class FindOneForId {
        @Test
        @DisplayName("returns an optional containing a given user's password")
        void returnsAnOptionalContainingAGivenUsersPassword() {
            UsersRecord user = userRecordPool.createAndSaveOne();
            Optional<String> foundPassword = userPasswordJooqRepository.findOneForId(user.getId());
            assertTrue(foundPassword.isPresent());
            assertEquals(user.getPassword(), foundPassword.get());
        }

        @Test
        @DisplayName("returns an empty optional for a non-valid user")
        void returnsAnEmptyOptionalForANonValidUser() {
            Optional<String> foundPassword = userPasswordJooqRepository.findOneForId(Long.MAX_VALUE);
            assertTrue(foundPassword.isEmpty());
        }
    }
}
