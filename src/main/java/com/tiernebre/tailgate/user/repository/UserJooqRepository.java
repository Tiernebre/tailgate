package com.tiernebre.tailgate.user.repository;

import com.tiernebre.tailgate.jooq.tables.records.UserSecurityQuestionsRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.token.refresh.RefreshTokenConfigurationProperties;
import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.tiernebre.tailgate.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class UserJooqRepository implements UserRepository {
    private final DSLContext dslContext;
    private final RefreshTokenConfigurationProperties refreshTokenConfigurationProperties;

    @Override
    public UserEntity createOne(CreateUserRequest createUserRequest) {
        return dslContext.transactionResult(configuration -> {
            DSLContext transactionContext = DSL.using(configuration);
            UsersRecord usersRecord = transactionContext.newRecord(USERS, createUserRequest);
            usersRecord.store();
            UserEntity userEntity = usersRecord.into(UserEntity.class);

            List<UserSecurityQuestionsRecord> securityQuestionsToCreate = createUserRequest
                    .getSecurityQuestions()
                    .stream()
                    .map(securityQuestionToCreate -> {
                        UserSecurityQuestionsRecord userSecurityQuestionsRecord = transactionContext.newRecord(USER_SECURITY_QUESTIONS);
                        userSecurityQuestionsRecord.setUserId(userEntity.getId());
                        userSecurityQuestionsRecord.setSecurityQuestionId(securityQuestionToCreate.getId());
                        userSecurityQuestionsRecord.setAnswer(securityQuestionToCreate.getAnswer());
                        return userSecurityQuestionsRecord;
                    })
                    .collect(Collectors.toList());
            transactionContext.batchStore(securityQuestionsToCreate).execute();

            return userEntity;
        });
    }

    @Override
    public Optional<UserEntity> findOneById(Long id) {
        return dslContext
                .selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public Optional<UserEntity> updateOne(UserEntity entity) {
        return dslContext
                .update(USERS)
                .set(USERS.EMAIL, entity.getEmail())
                .set(USERS.PASSWORD, entity.getPassword())
                .where(USERS.ID.eq(entity.getId()))
                .returning(USERS.asterisk())
                .fetchOptional()
                .map(usersRecord -> usersRecord.into(UserEntity.class));
    }

    @Override
    public Boolean deleteOneById(Long id) {
        int numberOfDeletedEntities = dslContext
                .delete(USERS)
                .where(USERS.ID.eq(id))
                .execute();
        return numberOfDeletedEntities == 1;
    }

    @Override
    public Optional<UserEntity> findOneByEmail(String email) {
        return dslContext
                .selectFrom(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public boolean oneExistsByEmail(String email) {
        return dslContext
                .fetchExists(
                        dslContext.selectFrom(USERS)
                                .where(USERS.EMAIL.eq(email))
                );
    }

    @Override
    public Optional<UserEntity> findOneWithNonExpiredRefreshToken(String refreshToken) {
        long refreshTokenExpireWindowInMilliseconds = TimeUnit.MINUTES.toMillis(refreshTokenConfigurationProperties.getExpirationWindowInMinutes());
        return dslContext.select(USERS.asterisk())
                .from(USERS)
                .join(REFRESH_TOKENS)
                .on(USERS.ID.eq(REFRESH_TOKENS.USER_ID))
                .where(REFRESH_TOKENS.TOKEN.eq(refreshToken))
                .and(REFRESH_TOKENS.CREATED_AT.add(refreshTokenExpireWindowInMilliseconds).greaterThan(LocalDateTime.now()))
                .fetchOptionalInto(UserEntity.class);
    }

    @Override
    public void updatePasswordForOneWithEmailAndNonExpiredPasswordResetToken(String password, String email, String passwordResetToken) {

    }
}
