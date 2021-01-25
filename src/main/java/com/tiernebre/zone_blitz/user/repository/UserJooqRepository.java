package com.tiernebre.zone_blitz.user.repository;

import com.tiernebre.zone_blitz.jooq.tables.records.UserRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserSecurityQuestionRecord;
import com.tiernebre.zone_blitz.token.refresh.RefreshTokenConfigurationProperties;
import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tiernebre.zone_blitz.jooq.Tables.*;
import static org.jooq.impl.DSL.localDateTimeAdd;

@Repository
@RequiredArgsConstructor
public class UserJooqRepository implements UserRepository {
    private final DSLContext dslContext;
    private final RefreshTokenConfigurationProperties refreshTokenConfigurationProperties;

    @Override
    public UserEntity createOne(CreateUserRequest createUserRequest) {
        return dslContext.transactionResult(configuration -> {
            DSLContext transactionContext = DSL.using(configuration);
            UserRecord usersRecord = transactionContext.newRecord(USER, createUserRequest);
            usersRecord.insert();
            usersRecord.refresh();
            UserEntity userEntity = usersRecord.into(UserEntity.class);

            List<UserSecurityQuestionRecord> securityQuestionsToCreate = createUserRequest
                    .getSecurityQuestions()
                    .stream()
                    .map(securityQuestionToCreate -> {
                        UserSecurityQuestionRecord userSecurityQuestionRecord = transactionContext.newRecord(USER_SECURITY_QUESTION);
                        userSecurityQuestionRecord.setUserId(userEntity.getId());
                        userSecurityQuestionRecord.setSecurityQuestionId(securityQuestionToCreate.getId());
                        userSecurityQuestionRecord.setAnswer(securityQuestionToCreate.getAnswer());
                        return userSecurityQuestionRecord;
                    })
                    .collect(Collectors.toList());
            transactionContext.batchStore(securityQuestionsToCreate).execute();

            return userEntity;
        });
    }

    @Override
    public Optional<UserEntity> findOneById(Long id) {
        return dslContext
                .selectFrom(USER)
                .where(USER.ID.eq(id))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public Optional<UserEntity> updateOne(UserEntity entity) {
        return dslContext
                .update(USER)
                .set(USER.EMAIL, entity.getEmail())
                .set(USER.PASSWORD, entity.getPassword())
                .where(USER.ID.eq(entity.getId()))
                .returning(USER.asterisk())
                .fetchOptional()
                .map(usersRecord -> usersRecord.into(UserEntity.class));
    }

    @Override
    public Boolean deleteOneById(Long id) {
        int numberOfDeletedEntities = dslContext
                .delete(USER)
                .where(USER.ID.eq(id))
                .execute();
        return numberOfDeletedEntities == 1;
    }

    @Override
    public Optional<UserEntity> findOneByEmail(String email) {
        return dslContext
                .selectFrom(USER)
                .where(USER.EMAIL.eq(email))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public boolean oneExistsByEmail(String email) {
        return dslContext
                .fetchExists(
                        dslContext.selectFrom(USER)
                                .where(USER.EMAIL.eq(email))
                );
    }

    @Override
    public Optional<UserEntity> findOneWithNonExpiredRefreshToken(UUID refreshToken) {
        return dslContext.select(USER.asterisk())
                .from(USER)
                .join(REFRESH_TOKEN)
                .on(USER.ID.eq(REFRESH_TOKEN.USER_ID))
                .where(REFRESH_TOKEN.TOKEN.eq(refreshToken))
                .and(
                        localDateTimeAdd(
                                REFRESH_TOKEN.CREATED_AT,
                                refreshTokenConfigurationProperties.getExpirationWindowInMinutes(),
                                DatePart.MINUTE
                        ).greaterThan(LocalDateTime.now())
                )
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public boolean confirmOne(UUID confirmationToken) {
        int numberOfConfirmedUser = dslContext
                .update(USER)
                .set(USER.IS_CONFIRMED, true)
                .from(USER_CONFIRMATION_TOKEN)
                .where(USER.ID.eq(USER_CONFIRMATION_TOKEN.USER_ID))
                .and(USER_CONFIRMATION_TOKEN.TOKEN.eq(confirmationToken))
                .execute();
        return numberOfConfirmedUser == 1;
    }
}
