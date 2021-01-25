package com.tiernebre.zone_blitz.security_questions;

import com.tiernebre.zone_blitz.jooq.tables.records.SecurityQuestionRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tiernebre.zone_blitz.jooq.Tables.USER_SECURITY_QUESTION;
import static com.tiernebre.zone_blitz.jooq.tables.SecurityQuestion.SECURITY_QUESTION;

@Component
@RequiredArgsConstructor
public class SecurityQuestionRecordPool {
    private final DSLContext dslContext;

    public List<SecurityQuestionRecord> getAll() {
        return dslContext.selectFrom(SECURITY_QUESTION).fetch();
    }

    public List<SecurityQuestionRecord> createMultiple() {
        List<SecurityQuestionRecord> securityQuestionsRecords = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            securityQuestionsRecords.add(createOne());
        }
        return securityQuestionsRecords;
    }

    public SecurityQuestionRecord createOne() {
        SecurityQuestionRecord securityQuestionsRecord = dslContext.newRecord(SECURITY_QUESTION);
        securityQuestionsRecord.setQuestion(UUID.randomUUID().toString());
        securityQuestionsRecord.store();
        return securityQuestionsRecord;
    }

    public void deleteAll() {
        dslContext.deleteFrom(SECURITY_QUESTION).execute();
    }

    public List<SecurityQuestionRecord> getSecurityQuestionForUser(UserRecord user) {
        return dslContext
                .select(SECURITY_QUESTION.asterisk())
                .from(SECURITY_QUESTION)
                .join(USER_SECURITY_QUESTION)
                .on(SECURITY_QUESTION.ID.eq(USER_SECURITY_QUESTION.SECURITY_QUESTION_ID))
                .where(USER_SECURITY_QUESTION.USER_ID.eq(user.getId()))
                .fetchInto(SecurityQuestionRecord.class);
    }
}
