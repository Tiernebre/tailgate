package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.jooq.tables.records.RefreshTokensRecord;
import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RefreshTokenJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private RefreshTokenRecordPool recordPool;

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
    }
}
