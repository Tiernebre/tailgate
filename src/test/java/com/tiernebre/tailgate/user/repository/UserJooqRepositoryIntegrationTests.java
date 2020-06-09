package com.tiernebre.tailgate.user.repository;

import com.tiernebre.tailgate.jooq.tables.records.RefreshTokensRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.test.DatabaseIntegrationTestSuite;
import com.tiernebre.tailgate.token.refresh.RefreshTokenConfigurationProperties;
import com.tiernebre.tailgate.token.refresh.RefreshTokenRecordPool;
import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.UserRecordPool;
import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class UserJooqRepositoryIntegrationTests extends DatabaseIntegrationTestSuite {
    @Autowired
    private UserJooqRepository userJooqRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private RefreshTokenRecordPool refreshTokenRecordPool;

    @Autowired
    private RefreshTokenConfigurationProperties refreshTokenConfigurationProperties;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the correctly mapped saved entity")
        void returnsTheCorrectlyMappedSavedEntity() {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest();
            UserEntity savedEntity = userJooqRepository.createOne(createUserRequest);
            assertAll(
                    () -> assertNotNull(savedEntity.getId()),
                    () -> assertEquals(createUserRequest.getEmail(), savedEntity.getEmail()),
                    () -> assertEquals(createUserRequest.getPassword(), savedEntity.getPassword())
            );
        }

        @Test
        @DisplayName("persists an entity onto the database")
        void persistsAnEntityOntoTheDatabase() {
            UserEntity savedEntity = userJooqRepository.createOne(UserFactory.generateOneCreateUserRequest());
            Boolean entityGotSaved = userRecordPool.oneExistsWithIdAndEmail(savedEntity.getId(), savedEntity.getEmail());
            assertTrue(entityGotSaved);
        }

        @Test
        @DisplayName("creates the security question answers for the user")
        void createsTheSecurityQuestionsForTheUser() {

        }
    }

    @Nested
    @DisplayName("findOneById")
    public class FindOneByIdTests {
        @Test
        @DisplayName("returns a non-empty Optional with the found entity with the correct attributes if the given ID exists")
        void testThatFindOneByIdReturnsAnExistingEntity() {
            UsersRecord savedExistingUser = userRecordPool.createAndSaveOne();
            UserEntity foundUser = userJooqRepository.findOneById(savedExistingUser.getId()).orElse(null);
            assertNotNull(foundUser);
            assertThatUsersRecordEqualsEntity(savedExistingUser, foundUser);
        }

        @Test
        @DisplayName("returns an empty Optional if the given ID does not exist")
        void testThatFindOneByIdReturnsEmptyForNonExistingEntity() {
            Optional<UserEntity> foundUser = userJooqRepository.findOneById(Long.MAX_VALUE);
            assertTrue(foundUser.isEmpty());
        }
    }

    @Nested
    @DisplayName("updateOne")
    public class UpdateOneTests {
        @Test
        @DisplayName("returns the updated version of the entity")
        void testThatUpdateOneReturnsTheUpdatedVersion() {
            UsersRecord savedExistingUser = userRecordPool.createAndSaveOne();
            UserEntity entityToUpdate = UserFactory.generateOneEntity(savedExistingUser.getId());
            UserEntity updatedEntity = userJooqRepository.updateOne(entityToUpdate).orElse(null);
            assertNotNull(updatedEntity);
            assertAll(() -> {
                assertEquals(savedExistingUser.getId(), updatedEntity.getId());
                assertEquals(entityToUpdate.getEmail(), updatedEntity.getEmail());
                assertEquals(entityToUpdate.getPassword(), updatedEntity.getPassword());
            });
        }

        @Test
        @DisplayName("returns an empty optional if the entity does not exist")
        void testThatUpdateOneReturnsAnEmptyOptionalIfTheEntityDoesNotExist() {
            UserEntity entityToUpdate = UserFactory.generateOneEntity(Long.MAX_VALUE);
            Optional<UserEntity> updatedEntity = userJooqRepository.updateOne(entityToUpdate);
            assertTrue(updatedEntity.isEmpty());
        }
    }

    @Nested
    @DisplayName("deleteOneById")
    public class DeleteOneByIdTests {
        @Test
        @DisplayName("returns true if the user to delete existed")
        void testThatDeleteOneByIdReturnsTrueForExistingEntitySuccess() {
            UsersRecord savedExistingUser = userRecordPool.createAndSaveOne();
            Boolean deleted = userJooqRepository.deleteOneById(savedExistingUser.getId());
            assertTrue(deleted);
        }

        @Test
        @DisplayName("returns false if the user to delete did not exist")
        void testThatDeleteOneByIdReturnsFalseForNonExistingEntityFailure() {
            Boolean deleted = userJooqRepository.deleteOneById(Long.MAX_VALUE);
            assertFalse(deleted);
        }
    }

    @Nested
    @DisplayName("findOneByEmail")
    public class FindOneByEmailTests {
        @Test
        @DisplayName("returns an optional containing the found user if the email exists")
        void testThatFindOneByEmailReturnsAnExistingUser() {
            UsersRecord savedExistingUser = userRecordPool.createAndSaveOne();
            UserEntity foundUser = userJooqRepository.findOneByEmail(savedExistingUser.getEmail()).orElse(null);
            assertNotNull(foundUser);
            assertThatUsersRecordEqualsEntity(savedExistingUser, foundUser);
        }

        @Test
        @DisplayName("returns an empty optional if the email does not exist")
        void testThatFindOneByEmailReturnsEmptyOptionalForNonExistentUser() {
            String nonExistentEmail = UUID.randomUUID().toString() + "-NON-EXISTENT@test.io";
            Optional<UserEntity> foundUser = userJooqRepository.findOneByEmail(nonExistentEmail);
            assertTrue(foundUser.isEmpty());
        }
    }

    @Nested
    @DisplayName("oneExistsByEmail")
    public class OneExistsByEmailTests {
        @Test
        @DisplayName("returns true if the email exists")
        void returnsTrueIfItExists() {
            UsersRecord savedExistingUser = userRecordPool.createAndSaveOne();
            assertTrue(userJooqRepository.oneExistsByEmail(savedExistingUser.getEmail()));
        }

        @Test
        @DisplayName("returns false if the email does not exist")
        void returnsFalseIfItDoesNotExist() {
            assertFalse(userJooqRepository.oneExistsByEmail("TOTALLY_NON_EXISTENT_EMAIL@NOT_A_THING.org"));
        }
    }

    @Nested
    @DisplayName("findOneWithNonExpiredRefreshToken")
    public class FindOneWithNonExpiredRefreshTokenTests {
        @Test
        @DisplayName("returns a user for a valid and non expired refresh token")
        void returnsAUserForAValidAndNonExpiredRefreshToken() {
            UsersRecord user = userRecordPool.createAndSaveOne();
            RefreshTokensRecord refreshToken = refreshTokenRecordPool.createAndSaveOneForUser(user);
            Optional<UserEntity> foundUser = userJooqRepository.findOneWithNonExpiredRefreshToken(refreshToken.getToken());
            assertTrue(foundUser.isPresent());
            assertThatUsersRecordEqualsEntity(user, foundUser.get());
        }

        @Test
        @DisplayName("returns an empty optional for a refresh token that does not exist")
        void returnsAnEmptyOptionalForANonExistentRefreshToken() {
            Optional<UserEntity> foundUser = userJooqRepository.findOneWithNonExpiredRefreshToken(UUID.randomUUID().toString());
            assertFalse(foundUser.isPresent());
        }

        @Test
        @DisplayName("returns an empty optional for a refresh token that is past the expiration window")
        void returnsAnEmptyOptionalForAnExpiredRefreshToken() {
            UsersRecord user = userRecordPool.createAndSaveOne();
            RefreshTokensRecord refreshToken = refreshTokenRecordPool.createAndSaveOneForUser(user);
            long expirationWindowInMilliseconds = TimeUnit.MINUTES.toMillis(refreshTokenConfigurationProperties.getExpirationWindowInMinutes());
            Instant expiredInstant = Instant.now().minusMillis(expirationWindowInMilliseconds + 1);
            refreshToken.setCreatedAt(LocalDateTime.ofInstant(expiredInstant, ZoneId.of("UTC")));
            refreshToken.store();
            Optional<UserEntity> foundUser = userJooqRepository.findOneWithNonExpiredRefreshToken(UUID.randomUUID().toString());
            assertFalse(foundUser.isPresent());
        }
    }

    private void assertThatUsersRecordEqualsEntity(UsersRecord usersRecord, UserEntity userEntity) {
        assertAll(() -> {
            assertEquals(usersRecord.getId(), userEntity.getId());
            assertEquals(usersRecord.getEmail(), userEntity.getEmail());
            assertEquals(usersRecord.getPassword(), userEntity.getPassword());
        });
    }
}
