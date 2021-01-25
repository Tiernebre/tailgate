package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.jooq.tables.records.PasswordResetTokenRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserRecord;
import com.tiernebre.zone_blitz.test.AbstractIntegrationTestingSuite;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class PasswordResetTokenJooqRepositoryIntegrationTests extends AbstractIntegrationTestingSuite {
    @Autowired
    private PasswordResetTokenJooqRepository passwordResetTokenJooqRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private PasswordResetTokenRecordPool passwordResetTokenRecordPool;

    @Nested
    @DisplayName("createOneForUser")
    public class CreateOneForUserTests {
        @Test
        @DisplayName("returns the created password reset token for a user")
        public void returnsTheCreatedPasswordResetTokenForAUser() {
            UserRecord userRecord = userRecordPool.createAndSaveOne();
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
            PasswordResetTokenRecord passwordResetTokensRecord = passwordResetTokenRecordPool.createAndSaveOne();
            passwordResetTokenJooqRepository.deleteOne(passwordResetTokensRecord.getToken());
            assertNull(passwordResetTokenRecordPool.getOneById(passwordResetTokensRecord.getToken()));
        }

        @Test
        @DisplayName("does not delete multiple password reset tokens accidentally")
        public void doesNotDeleteMultiplePasswordResetTokensAccidentally() {
            PasswordResetTokenRecord PasswordResetTokenRecordToDelete = passwordResetTokenRecordPool.createAndSaveOne();
            PasswordResetTokenRecord otherPasswordResetToken = passwordResetTokenRecordPool.createAndSaveOne();
            passwordResetTokenJooqRepository.deleteOne(PasswordResetTokenRecordToDelete.getToken());
            assertNull(passwordResetTokenRecordPool.getOneById(PasswordResetTokenRecordToDelete.getToken()));
            assertNotNull(passwordResetTokenRecordPool.getOneById(otherPasswordResetToken.getToken()));
        }
    }
}
