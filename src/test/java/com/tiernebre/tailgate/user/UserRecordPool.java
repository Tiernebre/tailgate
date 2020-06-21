package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.jooq.tables.records.SecurityQuestionsRecord;
import com.tiernebre.tailgate.jooq.tables.records.UserSecurityQuestionsRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.security_questions.SecurityQuestionRecordPool;
import com.tiernebre.tailgate.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tiernebre.tailgate.jooq.Tables.*;

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

    public UsersRecord createAndSaveOne() {
        UserEntity userEntity = UserFactory.generateOneEntity();
        UsersRecord usersRecord = dslContext.newRecord(USERS);
        usersRecord.setEmail(userEntity.getEmail());
        usersRecord.setPassword(userEntity.getPassword());
        usersRecord.store();
        return usersRecord;
    }

    public UsersRecord createAndSaveOneWithSecurityQuestions() {
        List<SecurityQuestionsRecord> securityQuestions = securityQuestionRecordPool.createMultiple();
        UserEntity userEntity = UserFactory.generateOneEntity();
        UsersRecord usersRecord = dslContext.newRecord(USERS);
        usersRecord.setEmail(userEntity.getEmail());
        usersRecord.setPassword(userEntity.getPassword());
        usersRecord.store();
        List<UserSecurityQuestionsRecord> securityQuestionAnswers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            UserSecurityQuestionsRecord userSecurityQuestionsRecord = dslContext.newRecord(USER_SECURITY_QUESTIONS);
            userSecurityQuestionsRecord.setUserId(usersRecord.getId());
            userSecurityQuestionsRecord.setSecurityQuestionId(securityQuestions.get(i).getId());
            userSecurityQuestionsRecord.setAnswer(UUID.randomUUID().toString());
            securityQuestionAnswers.add(userSecurityQuestionsRecord);
        }
        dslContext.batchStore(securityQuestionAnswers).execute();
        return usersRecord;
    }

    public Boolean oneExistsWithIdAndEmail(Long id, String email) {
        return dslContext.fetchExists(dslContext.selectFrom(USERS).where(USERS.ID.eq(id)).and(USERS.EMAIL.eq(email)));
    }

    public Boolean oneExistsWithEmail(String email) {
        return dslContext.fetchExists(dslContext.selectFrom(USERS).where(USERS.EMAIL.eq(email)));
    }

    public UsersRecord findOneByIdAndEmail(Long id, String email) {
        return dslContext.selectFrom(USERS).where(USERS.ID.eq(id).and(USERS.EMAIL.eq(email))).fetchAny();
    }

    public List<UserSecurityQuestionsRecord> getSecurityQuestionsForUserWithId(Long id) {
        return dslContext.selectFrom(USER_SECURITY_QUESTIONS).where(USER_SECURITY_QUESTIONS.USER_ID.eq(id)).fetch();
    }

    public void deleteAll() {
        dslContext.deleteFrom(USER_CONFIRMATION_TOKENS).execute();
        dslContext.deleteFrom(REFRESH_TOKENS).execute();
        dslContext.deleteFrom(USER_SECURITY_QUESTIONS).execute();
        dslContext.deleteFrom(USERS).execute();
        securityQuestionRecordPool.deleteAll();
    }
}
