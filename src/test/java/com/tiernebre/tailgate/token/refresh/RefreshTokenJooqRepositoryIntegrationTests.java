package com.tiernebre.tailgate.token.refresh;

import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserRecordPool;
import com.tiernebre.tailgate.validator.StringIsBlankException;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tiernebre.tailgate.token.refresh.RefreshTokenConstants.BLANK_TOKEN_ERROR_MESSAGE;
import static com.tiernebre.tailgate.token.refresh.RefreshTokenConstants.NULL_USER_ERROR_MESSAGE;
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

        @ParameterizedTest(name = "throws a StringIsBlankException with a helpful message if the token to delete is \"{0}\"")
        @NullSource
        @ValueSource(strings = { "", " " })
        void throwsStringIsBlankExceptionWithAHelpfulMessageIfTheTokenToDeleteIs(String tokenToDelete) {
            StringIsBlankException thrownException = assertThrows(StringIsBlankException.class, () -> refreshTokenRepository.deleteOne(tokenToDelete));
            assertEquals(BLANK_TOKEN_ERROR_MESSAGE, thrownException.getMessage());
        }
    }
}
