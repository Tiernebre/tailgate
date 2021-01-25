package com.tiernebre.zone_blitz.user;

import com.tiernebre.zone_blitz.jooq.tables.records.SecurityQuestionRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserSecurityQuestionRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserRecord;
import com.tiernebre.zone_blitz.security_questions.SecurityQuestionRecordPool;
import com.tiernebre.zone_blitz.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tiernebre.zone_blitz.jooq.Tables.*;

/**
 * Manages data within the jooq data context that is used within the integration tests.
 *
 * Entirely used for scenarios where we want to avoid the repository equivalent of doing something, as this
 * lets us test a single individual method at a time (leading to less noisy test errors if one repository method
 * breaks).
 */
@Component
@RequiredArgsConstructor
public class UserRecordPool {
    private final DSLContext dslContext;
    private final SecurityQuestionRecordPool securityQuestionRecordPool;

    public UserRecord createAndSaveOne() {
        UserEntity userEntity = UserFactory.generateOneEntity();
        UserRecord usersRecord = dslContext.newRecord(USER);
        usersRecord.setEmail(userEntity.getEmail());
        usersRecord.setPassword(userEntity.getPassword());
        usersRecord.setIsConfirmed(false);
        usersRecord.store();
        return usersRecord;
    }

    public UserRecord createAndSaveOneWithSecurityQuestion() {
        List<SecurityQuestionRecord> securityQuestions = securityQuestionRecordPool.createMultiple();
        UserEntity userEntity = UserFactory.generateOneEntity();
        UserRecord usersRecord = dslContext.newRecord(USER);
        usersRecord.setEmail(userEntity.getEmail());
        usersRecord.setPassword(userEntity.getPassword());
        usersRecord.store();
        List<UserSecurityQuestionRecord> securityQuestionAnswers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            UserSecurityQuestionRecord userSecurityQuestionRecord = dslContext.newRecord(USER_SECURITY_QUESTION);
            userSecurityQuestionRecord.setUserId(usersRecord.getId());
            userSecurityQuestionRecord.setSecurityQuestionId(securityQuestions.get(i).getId());
            userSecurityQuestionRecord.setAnswer(UUID.randomUUID().toString());
            securityQuestionAnswers.add(userSecurityQuestionRecord);
        }
        dslContext.batchStore(securityQuestionAnswers).execute();
        return usersRecord;
    }

    public Boolean oneExistsWithIdAndEmail(Long id, String email) {
        return dslContext.fetchExists(dslContext.selectFrom(USER).where(USER.ID.eq(id)).and(USER.EMAIL.eq(email)));
    }

    public Boolean oneExistsWithEmail(String email) {
        return dslContext.fetchExists(dslContext.selectFrom(USER).where(USER.EMAIL.eq(email)));
    }

    public UserRecord findOneByIdAndEmail(Long id, String email) {
        return dslContext.selectFrom(USER).where(USER.ID.eq(id).and(USER.EMAIL.eq(email))).fetchAny();
    }

    public List<UserSecurityQuestionRecord> getSecurityQuestionForUserWithId(Long id) {
        return dslContext.selectFrom(USER_SECURITY_QUESTION).where(USER_SECURITY_QUESTION.USER_ID.eq(id)).fetch();
    }
}
