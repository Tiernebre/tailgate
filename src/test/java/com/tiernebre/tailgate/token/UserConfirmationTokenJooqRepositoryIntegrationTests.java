package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.jooq.tables.records.UserConfirmationTokensRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserRecordPool;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserConfirmationTokenJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private UserConfirmationTokenJooqRepository userConfirmationTokenJooqRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private UserConfirmationTokenRecordPool userConfirmationTokenRecordPool;

    @Nested
    @DisplayName("createOneForUser")
    public class CreateOneForUserTests {
        @Test
        @DisplayName("returns the created invite token for a user")
        public void returnsTheCreatedInviteTokenForAUser() {
            UsersRecord userRecord = userRecordPool.createAndSaveOne();
            UserDto user = UserDto.builder().id(userRecord.getId()).build();
            UserConfirmationTokenEntity refreshTokenEntity = userConfirmationTokenJooqRepository.createOneForUser(user);
            assertNotNull(refreshTokenEntity.getToken());
            assertTrue(StringUtils.isNotBlank(refreshTokenEntity.getToken()));
            assertNotNull(refreshTokenEntity.getCreatedAt());
            assertEquals(user.getId(), refreshTokenEntity.getUserId());
        }

        @Test
        @DisplayName("only allows one invite token for a user")
        public void onlyAllowsOneInviteTokenForAUser() {
            UsersRecord userRecord = userRecordPool.createAndSaveOne();
            UserDto user = UserDto.builder().id(userRecord.getId()).build();
            userConfirmationTokenJooqRepository.createOneForUser(user);
            assertThrows(Exception.class, () -> userConfirmationTokenJooqRepository.createOneForUser(user));
        }
    }

    @Nested
    @DisplayName("deleteOne")
    public class deleteOneTests {
        @Test
        @DisplayName("deletes the given invite token value")
        public void deletesTheGivenInviteTokenValue() {
            UserConfirmationTokensRecord userConfirmationTokensRecord = userConfirmationTokenRecordPool.createAndSaveOne();
            userConfirmationTokenJooqRepository.deleteOne(userConfirmationTokensRecord.getToken());
            assertNull(userConfirmationTokenRecordPool.getOneById(userConfirmationTokensRecord.getToken()));
        }

        @Test
        @DisplayName("does not delete multiple invite tokens accidentally")
        public void doesNotDeleteMultipleInviteTokensAccidentally() {
            UserConfirmationTokensRecord userConfirmationTokensRecordToDelete = userConfirmationTokenRecordPool.createAndSaveOne();
            UserConfirmationTokensRecord otherUserConfirmationToken = userConfirmationTokenRecordPool.createAndSaveOne();
            userConfirmationTokenJooqRepository.deleteOne(userConfirmationTokensRecordToDelete.getToken());
            assertNull(userConfirmationTokenRecordPool.getOneById(userConfirmationTokensRecordToDelete.getToken()));
            assertNotNull(userConfirmationTokenRecordPool.getOneById(otherUserConfirmationToken.getToken()));
        }
    }
}
