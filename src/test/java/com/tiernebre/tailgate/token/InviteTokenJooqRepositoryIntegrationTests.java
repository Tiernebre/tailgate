package com.tiernebre.tailgate.token;

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

public class InviteTokenJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private InviteTokenJooqRepository inviteTokenJooqRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Nested
    @DisplayName("createOneForUser")
    public class CreateOneForUserTests {
        @Test
        @DisplayName("returns the created invite token for a user")
        public void returnsTheCreatedInviteTokenForAUser() {
            UsersRecord userRecord = userRecordPool.createAndSaveOne();
            UserDto user = UserDto.builder().id(userRecord.getId()).build();
            InviteTokenEntity refreshTokenEntity = inviteTokenJooqRepository.createOneForUser(user);
            assertNotNull(refreshTokenEntity.getToken());
            assertTrue(StringUtils.isNotBlank(refreshTokenEntity.getToken()));
            assertNotNull(refreshTokenEntity.getCreatedAt());
            assertEquals(user.getId(), refreshTokenEntity.getUserId());
        }
    }
}
