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

public class RefreshTokenJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private RefreshTokenRecordPool refreshTokenRecordPool;

    @Nested
    class CreateOneForUserTests {
        @Test
        @DisplayName("returns the saved refresh token as an entity")
        void returnsTheSavedRefreshToken() {
            UsersRecord userRecord = userRecordPool.createAndSaveOne();
            UserDto user = UserDto.builder().id(userRecord.getId()).build();
            RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.createOneForUser(user);
            assertNotNull(refreshTokenEntity.getToken());
            assertTrue(StringUtils.isNotBlank(refreshTokenEntity.getToken()));
            assertNotNull(refreshTokenEntity.getCreatedAt());
            assertEquals(user.getId(), refreshTokenEntity.getUserId());
        }
    }

    @Nested
    class DeleteOneTests {
        @Test
        @DisplayName("deletes the given refresh token")
        void deletesTheGivenRefreshToken() {
            String refreshTokenToDelete = refreshTokenRecordPool.createAndSaveOne().getToken();
            assertNotNull(refreshTokenRecordPool.getOneById(refreshTokenToDelete));
            refreshTokenRepository.deleteOne(refreshTokenToDelete);
            assertNull(refreshTokenRecordPool.getOneById(refreshTokenToDelete));
        }

        @Test
        @DisplayName("only deletes the given refresh token")
        void onlyDeletesTheGivenRefreshToken() {
            String refreshTokenToDelete = refreshTokenRecordPool.createAndSaveOne().getToken();
            String refreshTokenToNotDelete = refreshTokenRecordPool.createAndSaveOne().getToken();
            assertNotNull(refreshTokenRecordPool.getOneById(refreshTokenToDelete));
            assertNotNull(refreshTokenRecordPool.getOneById(refreshTokenToNotDelete));
            refreshTokenRepository.deleteOne(refreshTokenToDelete);
            assertNull(refreshTokenRecordPool.getOneById(refreshTokenToDelete));
            assertNotNull(refreshTokenRecordPool.getOneById(refreshTokenToNotDelete));
        }
    }
}
