package com.tiernebre.zone_blitz.token.user_confirmation;

import com.tiernebre.zone_blitz.jooq.tables.records.UserConfirmationTokensRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UsersRecord;
import com.tiernebre.zone_blitz.test.DatabaseIntegrationTestSuite;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

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
            UserConfirmationTokenEntity confirmationTokenEntity = userConfirmationTokenJooqRepository.createOneForUser(user);
            assertNotNull(confirmationTokenEntity.getToken());
            assertTrue(StringUtils.isNotBlank(confirmationTokenEntity.getToken()));
            assertNotNull(confirmationTokenEntity.getCreatedAt());
            assertEquals(user.getId(), confirmationTokenEntity.getUserId());
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
    @DisplayName("findOneForUserTests")
    public class FindOneForUserTests {
        @Test
        @DisplayName("returns an optional containing the found confirmation token if it exists")
        public void returnsAnOptionalContainingTheFoundConfirmationTokenIfItExists() {
            UsersRecord userRecord = userRecordPool.createAndSaveOne();
            UserConfirmationTokensRecord confirmationToken = userConfirmationTokenRecordPool.createAndSaveOneForUser(userRecord);
            UserDto user = UserDto.builder().id(userRecord.getId()).build();
            Optional<UserConfirmationTokenEntity> foundConfirmationToken = userConfirmationTokenJooqRepository.findOneForUser(user);
            assertTrue(foundConfirmationToken.isPresent());
            assertEquals(confirmationToken.getToken(), foundConfirmationToken.get().getToken());
        }
    }
}
