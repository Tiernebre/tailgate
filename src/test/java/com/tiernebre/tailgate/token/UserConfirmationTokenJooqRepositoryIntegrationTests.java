package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.jooq.tables.records.InviteTokensRecord;
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
    private InviteTokenRecordPool inviteTokenRecordPool;

    @Nested
    @DisplayName("createOneForUser")
    public class CreateOneForUserTests {
        @Test
        @DisplayName("returns the created invite token for a user")
        public void returnsTheCreatedInviteTokenForAUser() {
            UsersRecord userRecord = userRecordPool.createAndSaveOne();
            UserDto user = UserDto.builder().id(userRecord.getId()).build();
            InviteTokenEntity refreshTokenEntity = userConfirmationTokenJooqRepository.createOneForUser(user);
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
            InviteTokensRecord inviteToken = inviteTokenRecordPool.createAndSaveOne();
            userConfirmationTokenJooqRepository.deleteOne(inviteToken.getToken());
            assertNull(inviteTokenRecordPool.getOneById(inviteToken.getToken()));
        }

        @Test
        @DisplayName("does not delete multiple invite tokens accidentally")
        public void doesNotDeleteMultipleInviteTokensAccidentally() {
            InviteTokensRecord inviteTokenToDelete = inviteTokenRecordPool.createAndSaveOne();
            InviteTokensRecord otherInviteToken = inviteTokenRecordPool.createAndSaveOne();
            userConfirmationTokenJooqRepository.deleteOne(inviteTokenToDelete.getToken());
            assertNull(inviteTokenRecordPool.getOneById(inviteTokenToDelete.getToken()));
            assertNotNull(inviteTokenRecordPool.getOneById(otherInviteToken.getToken()));
        }
    }
}
