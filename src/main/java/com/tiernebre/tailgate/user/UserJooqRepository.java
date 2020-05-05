package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.tiernebre.tailgate.jooq.Tables.USERS;

@Repository
@RequiredArgsConstructor
public class UserJooqRepository implements UserRepository {
    @Autowired
    private final DSLContext dslContext;

    @Override
    public UserEntity saveOne(UserEntity entity) {
        UsersRecord usersRecord = dslContext.newRecord(USERS);
        usersRecord.setEmail(entity.getEmail());
        usersRecord.setPassword(entity.getPassword());
        usersRecord.store();
        return usersRecord.into(UserEntity.class);
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
}
