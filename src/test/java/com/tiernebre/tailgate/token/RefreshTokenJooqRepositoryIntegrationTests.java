package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.jooq.tables.records.RefreshTokensRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserRecordPool;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;

public class RefreshTokenJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenRecordPool recordPool;

    @Autowired
    private UserRecordPool userRecordPool;

    @Nested
    class CreateOneForUserTests {
        @Test
        @DisplayName("returns the saved refresh token as an entity")
        public void returnsTheSavedRefreshToken() {
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
    class FindOneByIdTests {
        @Test
        @DisplayName("returns an optional with a found refresh token by its value")
        public void returnsAnExistingOptionalIfFound() {
            RefreshTokensRecord existingRefreshToken = recordPool.createAndSaveOne();
            Optional<RefreshTokenEntity> foundRefreshToken = refreshTokenRepository.findOneById(existingRefreshToken.getToken());
            assertTrue(foundRefreshToken.isPresent());
            assertEquals(existingRefreshToken.getToken(), foundRefreshToken.get().getToken());
        }

        @Test
        @DisplayName("returns an empty optional if an id is used that does not exist")
        public void returnsAnEmptyOptionalForANonExistentRefreshToken() {
            Optional<RefreshTokenEntity> foundRefreshToken = refreshTokenRepository.findOneById(UUID.randomUUID().toString());
            assertFalse(foundRefreshToken.isPresent());
        }
    }
}
