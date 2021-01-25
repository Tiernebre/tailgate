package com.tiernebre.zone_blitz.user.repository;

import com.tiernebre.zone_blitz.jooq.tables.records.RefreshTokenRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.SecurityQuestionRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserSecurityQuestionRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserRecord;
import com.tiernebre.zone_blitz.security_questions.SecurityQuestionRecordPool;
import com.tiernebre.zone_blitz.test.AbstractIntegrationTestingSuite;
import com.tiernebre.zone_blitz.token.refresh.RefreshTokenConfigurationProperties;
import com.tiernebre.zone_blitz.token.refresh.RefreshTokenRecordPool;
import com.tiernebre.zone_blitz.token.user_confirmation.UserConfirmationTokenRecordPool;
import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.entity.UserEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class UserJooqRepositoryIntegrationTests extends AbstractIntegrationTestingSuite {
    @Autowired
    private UserJooqRepository userJooqRepository;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private RefreshTokenRecordPool refreshTokenRecordPool;

    @Autowired
    private RefreshTokenConfigurationProperties refreshTokenConfigurationProperties;

    @Autowired
    private SecurityQuestionRecordPool securityQuestionRecordPool;

    @Autowired
    private UserConfirmationTokenRecordPool confirmationTokenRecordPool;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the correctly mapped saved entity")
        void returnsTheCorrectlyMappedSavedEntity() {
            CreateUserRequest createUserRequest = generateValidUserRequest();
            UserEntity savedEntity = userJooqRepository.createOne(createUserRequest);
            assertAll(
                    () -> assertNotNull(savedEntity.getId()),
                    () -> assertEquals(createUserRequest.getEmail(), savedEntity.getEmail()),
                    () -> assertEquals(createUserRequest.getPassword(), savedEntity.getPassword()),
                    () -> assertFalse(savedEntity.isConfirmed())
            );
        }

        @Test
        @DisplayName("persists a user entity onto the database")
        void persistsAnEntityOntoTheDatabase() {
            UserEntity savedEntity = userJooqRepository.createOne(generateValidUserRequest());
            assertTrue(userRecordPool.oneExistsWithIdAndEmail(savedEntity.getId(), savedEntity.getEmail()));
        }

        @Test
        @DisplayName("does not persist a user entity onto the database if an invalid security question is passed")
        void doesNotPersistAUserEntityOntoTheDatabaseIfAnInvalidSecurityQuestionIsPassed() {
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest(ImmutableSet.of(Long.MAX_VALUE));
            assertThrows(Exception.class, () -> userJooqRepository.createOne(createUserRequest));
            assertFalse(userRecordPool.oneExistsWithEmail(createUserRequest.getEmail()));
        }

        @Test
        @DisplayName("creates the security question answers for the user")
        void createsTheSecurityQuestionForTheUser() {
            List<SecurityQuestionRecord> securityQuestionsCreated = securityQuestionRecordPool.createMultiple();
            Set<Long> securityQuestionIds = securityQuestionsCreated
                    .stream()
                    .map(SecurityQuestionRecord::getId)
                    .collect(Collectors.toSet());
            CreateUserRequest createUserRequest = UserFactory.generateOneCreateUserRequest(securityQuestionIds);
            Long userId = userJooqRepository.createOne(createUserRequest).getId();
            List<UserSecurityQuestionRecord> expectedSecurityQuestion = createUserRequest
                    .getSecurityQuestions()
                    .stream()
                    .map(securityQuestion -> {
                        UserSecurityQuestionRecord expectedRecord = new UserSecurityQuestionRecord();
                        expectedRecord.setUserId(userId);
                        expectedRecord.setSecurityQuestionId(securityQuestion.getId());
                        expectedRecord.setAnswer(securityQuestion.getAnswer());
                        return expectedRecord;
                    })
                    .collect(Collectors.toList());

            List<UserSecurityQuestionRecord> foundSecurityQuestion = userRecordPool.getSecurityQuestionForUserWithId(userId);
            assertAll(
                    () -> assertTrue(CollectionUtils.isNotEmpty(expectedSecurityQuestion)),
                    () -> assertTrue(CollectionUtils.isNotEmpty(foundSecurityQuestion))
            );
            assertEquals(expectedSecurityQuestion, foundSecurityQuestion);
        }

        private CreateUserRequest generateValidUserRequest() {
            List<SecurityQuestionRecord> securityQuestionsCreated = securityQuestionRecordPool.createMultiple();
            Set<Long> securityQuestionIds = securityQuestionsCreated
                    .stream()
                    .map(SecurityQuestionRecord::getId)
                    .collect(Collectors.toSet());
            return UserFactory.generateOneCreateUserRequest(securityQuestionIds);
        }
    }

    @Nested
    @DisplayName("findOneById")
    public class FindOneByIdTests {
        @Test
        @DisplayName("returns a non-empty Optional with the found entity with the correct attributes if the given ID exists")
        void testThatFindOneByIdReturnsAnExistingEntity() {
            UserRecord savedExistingUser = userRecordPool.createAndSaveOne();
            UserEntity foundUser = userJooqRepository.findOneById(savedExistingUser.getId()).orElse(null);
            assertNotNull(foundUser);
            assertThatUserRecordEqualsEntity(savedExistingUser, foundUser);
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
            UserRecord savedExistingUser = userRecordPool.createAndSaveOne();
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
            UserRecord savedExistingUser = userRecordPool.createAndSaveOne();
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
            UserRecord savedExistingUser = userRecordPool.createAndSaveOne();
            UserEntity foundUser = userJooqRepository.findOneByEmail(savedExistingUser.getEmail()).orElse(null);
            assertNotNull(foundUser);
            assertThatUserRecordEqualsEntity(savedExistingUser, foundUser);
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
            UserRecord savedExistingUser = userRecordPool.createAndSaveOne();
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
            UserRecord user = userRecordPool.createAndSaveOne();
            RefreshTokenRecord refreshToken = refreshTokenRecordPool.createAndSaveOneForUser(user);
            Optional<UserEntity> foundUser = userJooqRepository.findOneWithNonExpiredRefreshToken(refreshToken.getToken());
            assertTrue(foundUser.isPresent());
            assertThatUserRecordEqualsEntity(user, foundUser.get());
        }

        @Test
        @DisplayName("returns an empty optional for a refresh token that does not exist")
        void returnsAnEmptyOptionalForANonExistentRefreshToken() {
            Optional<UserEntity> foundUser = userJooqRepository.findOneWithNonExpiredRefreshToken(UUID.randomUUID());
            assertFalse(foundUser.isPresent());
        }

        @Test
        @DisplayName("returns an empty optional for a refresh token that is past the expiration window")
        void returnsAnEmptyOptionalForAnExpiredRefreshToken() {
            UserRecord user = userRecordPool.createAndSaveOne();
            RefreshTokenRecord refreshToken = refreshTokenRecordPool.createAndSaveOneForUser(user);
            refreshToken.setCreatedAt(LocalDateTime
                    .now()
                    .minusMinutes(refreshTokenConfigurationProperties.getExpirationWindowInMinutes())
                    .minusSeconds(1)
            );
            refreshToken.store();
            Optional<UserEntity> foundUser = userJooqRepository.findOneWithNonExpiredRefreshToken(refreshToken.getToken());
            assertFalse(foundUser.isPresent());
        }
    }

    @Nested
    @DisplayName("confirmOne")
    public class ConfirmOneTests {
        @Test
        @DisplayName("confirms a user")
        void setsAUserIsConfirmedToTrue() {
            UserRecord user = userRecordPool.createAndSaveOne();
            assertFalse(user.getIsConfirmed());
            UUID confirmationToken = confirmationTokenRecordPool.createAndSaveOneForUser(user).getToken();
            userJooqRepository.confirmOne(confirmationToken);
            user.refresh();
            assertTrue(user.getIsConfirmed());
        }

        @Test
        @DisplayName("does not confirm multiple users on accident")
        void doesNotConfirmMultipleUserOnAccident() {
            UserRecord userToConfirm = userRecordPool.createAndSaveOne();
            UserRecord userToNotConfirm = userRecordPool.createAndSaveOne();
            assertFalse(userToConfirm.getIsConfirmed());
            assertFalse(userToNotConfirm.getIsConfirmed());
            UUID confirmationToken = confirmationTokenRecordPool.createAndSaveOneForUser(userToConfirm).getToken();
            confirmationTokenRecordPool.createAndSaveOneForUser(userToNotConfirm);
            userJooqRepository.confirmOne(confirmationToken);
            userToConfirm.refresh();
            userToNotConfirm.refresh();
            assertTrue(userToConfirm.getIsConfirmed());
            assertFalse(userToNotConfirm.getIsConfirmed());
        }

        @Test
        @DisplayName("returns true if a user was confirmed")
        void returnsTrueIfAUserWasConfirmed() {
            UserRecord user = userRecordPool.createAndSaveOne();
            assertFalse(user.getIsConfirmed());
            UUID confirmationToken = confirmationTokenRecordPool.createAndSaveOneForUser(user).getToken();
            assertTrue(userJooqRepository.confirmOne(confirmationToken));
        }

        @Test
        @DisplayName("returns false if a user was not confirmed")
        void returnsFalseIfAUserWasNotConfirmed() {
            assertFalse(userJooqRepository.confirmOne(UUID.randomUUID()));
        }
    }

    private void assertThatUserRecordEqualsEntity(UserRecord usersRecord, UserEntity userEntity) {
        assertAll(() -> {
            assertEquals(usersRecord.getId(), userEntity.getId());
            assertEquals(usersRecord.getEmail(), userEntity.getEmail());
            assertEquals(usersRecord.getPassword(), userEntity.getPassword());
        });
    }
}
