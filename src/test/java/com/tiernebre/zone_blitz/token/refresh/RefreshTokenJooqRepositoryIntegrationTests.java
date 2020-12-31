package com.tiernebre.zone_blitz.token.refresh;

import com.tiernebre.zone_blitz.jooq.tables.records.UsersRecord;
import com.tiernebre.zone_blitz.test.AbstractIntegrationTestingSuite;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.tiernebre.zone_blitz.token.refresh.RefreshTokenConstants.NULL_USER_ERROR_MESSAGE;
import static org.junit.Assert.*;

public class RefreshTokenJooqRepositoryIntegrationTests extends AbstractIntegrationTestingSuite {
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
            assertNotNull(refreshTokenEntity.getCreatedAt());
            assertEquals(user.getId(), refreshTokenEntity.getUserId());
        }

        @Test
        @DisplayName("throws a NullPointerException with a helpful message if the user to create a token for is null")
        void throwsANullPointerExceptionWithAHelpfulMessageIfTheUserToCreateATokenForIsNull() {
            NullPointerException thrownException = assertThrows(NullPointerException.class, () -> refreshTokenRepository.createOneForUser(null));
            assertEquals(NULL_USER_ERROR_MESSAGE, thrownException.getMessage());
        }
    }

    @Nested
    class DeleteOneTests {
        @Test
        @DisplayName("deletes the given refresh token")
        void deletesTheGivenRefreshToken() {
            UUID refreshTokenToDelete = refreshTokenRecordPool.createAndSaveOne().getToken();
            assertNotNull(refreshTokenRecordPool.getOneById(refreshTokenToDelete));
            refreshTokenRepository.deleteOne(refreshTokenToDelete);
            assertNull(refreshTokenRecordPool.getOneById(refreshTokenToDelete));
        }

        @Test
        @DisplayName("only deletes the given refresh token")
        void onlyDeletesTheGivenRefreshToken() {
            UUID refreshTokenToDelete = refreshTokenRecordPool.createAndSaveOne().getToken();
            UUID refreshTokenToNotDelete = refreshTokenRecordPool.createAndSaveOne().getToken();
            assertNotNull(refreshTokenRecordPool.getOneById(refreshTokenToDelete));
            assertNotNull(refreshTokenRecordPool.getOneById(refreshTokenToNotDelete));
            refreshTokenRepository.deleteOne(refreshTokenToDelete);
            assertNull(refreshTokenRecordPool.getOneById(refreshTokenToDelete));
            assertNotNull(refreshTokenRecordPool.getOneById(refreshTokenToNotDelete));
        }
    }
}
