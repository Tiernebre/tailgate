package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.tiernebre.tailgate.jooq.Tables.USERS;

/**
 * Manages data within the jooq data context that is used within the integration tests.
 *
 * Entirely used for scenarios where we want to avoid the repository equivalent of doing something, as this
 * lets us test a single individual method at a time (leading to less noisy test errors if one repository method
 * breaks).
 */
@Component
public class UserRecordPool {
    @Autowired
    private DSLContext dslContext;

    public UsersRecord createAndSaveOne() {
        UserEntity userEntity = UserFactory.generateOneEntity();
        UsersRecord usersRecord = dslContext.newRecord(USERS);
        usersRecord.setEmail(userEntity.getEmail());
        usersRecord.setPassword(userEntity.getPassword());
        usersRecord.store();
        return usersRecord;
    }

    public Boolean oneExistsWithIdAndEmail(Long id, String email) {
        return dslContext.fetchExists(dslContext.selectFrom(USERS).where(USERS.ID.eq(id)).and(USERS.EMAIL.eq(email)));
    }

    public UsersRecord findOneByIdAndEmail(Long id, String email) {
        return dslContext.selectFrom(USERS).where(USERS.ID.eq(id).and(USERS.EMAIL.eq(email))).fetchAny();
    }
}
