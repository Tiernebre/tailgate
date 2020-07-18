package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.jooq.tables.records.PasswordResetTokensRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UsersRecord;
import com.tiernebre.zone_blitz.test.DatabaseIntegrationTestSuite;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class PasswordResetTokenJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private PasswordResetTokenJooqRepository passwordResetTokenJooqRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private PasswordResetTokenRecordPool passwordResetTokenRecordPool;

    @AfterEach
    public void cleanup() {
        passwordResetTokenRecordPool.deleteAll();
    }

    @Nested
    @DisplayName("createOneForUser")
    public class CreateOneForUserTests {
        @Test
        @DisplayName("returns the created password reset token for a user")
        public void returnsTheCreatedPasswordResetTokenForAUser() {
            UsersRecord userRecord = userRecordPool.createAndSaveOne();
            UserDto user = UserDto.builder().id(userRecord.getId()).build();
            PasswordResetTokenEntity refreshTokenEntity = passwordResetTokenJooqRepository.createOneForUser(user);
            assertNotNull(refreshTokenEntity.getToken());
            assertNotNull(refreshTokenEntity.getCreatedAt());
            assertEquals(user.getId(), refreshTokenEntity.getUserId());
        }
    }

    @Nested
    @DisplayName("deleteOne")
    public class deleteOneTests {
        @Test
        @DisplayName("deletes the given password reset token value")
        public void deletesTheGivenPasswordResetTokenValue() {
            PasswordResetTokensRecord passwordResetTokensRecord = passwordResetTokenRecordPool.createAndSaveOne();
            passwordResetTokenJooqRepository.deleteOne(passwordResetTokensRecord.getToken());
            assertNull(passwordResetTokenRecordPool.getOneById(passwordResetTokensRecord.getToken()));
        }

        @Test
        @DisplayName("does not delete multiple password reset tokens accidentally")
        public void doesNotDeleteMultiplePasswordResetTokensAccidentally() {
            PasswordResetTokensRecord PasswordResetTokensRecordToDelete = passwordResetTokenRecordPool.createAndSaveOne();
            PasswordResetTokensRecord otherPasswordResetToken = passwordResetTokenRecordPool.createAndSaveOne();
            passwordResetTokenJooqRepository.deleteOne(PasswordResetTokensRecordToDelete.getToken());
            assertNull(passwordResetTokenRecordPool.getOneById(PasswordResetTokensRecordToDelete.getToken()));
            assertNotNull(passwordResetTokenRecordPool.getOneById(otherPasswordResetToken.getToken()));
        }
    }
}
