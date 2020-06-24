package com.tiernebre.tailgate.security_questions;

import com.tiernebre.tailgate.jooq.tables.records.SecurityQuestionsRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tiernebre.tailgate.jooq.Tables.USER_SECURITY_QUESTIONS;
import static com.tiernebre.tailgate.jooq.tables.SecurityQuestions.SECURITY_QUESTIONS;

@Component
@RequiredArgsConstructor
public class SecurityQuestionRecordPool {
    private final DSLContext dslContext;

    public List<SecurityQuestionsRecord> createMultiple() {
        List<SecurityQuestionsRecord> securityQuestionsRecords = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            securityQuestionsRecords.add(createOne());
        }
        return securityQuestionsRecords;
    }

    public SecurityQuestionsRecord createOne() {
        SecurityQuestionsRecord securityQuestionsRecord = dslContext.newRecord(SECURITY_QUESTIONS);
        securityQuestionsRecord.setQuestion(UUID.randomUUID().toString());
        securityQuestionsRecord.store();
        return securityQuestionsRecord;
    }

    public void deleteAll() {
        dslContext.deleteFrom(SECURITY_QUESTIONS).execute();
    }

    public List<SecurityQuestionsRecord> getSecurityQuestionsForUser(UsersRecord user) {
        return dslContext
                .select(SECURITY_QUESTIONS.asterisk())
                .from(SECURITY_QUESTIONS)
                .join(USER_SECURITY_QUESTIONS)
                .on(SECURITY_QUESTIONS.ID.eq(USER_SECURITY_QUESTIONS.SECURITY_QUESTION_ID))
                .where(USER_SECURITY_QUESTIONS.USER_ID.eq(user.getId()))
                .fetchInto(SecurityQuestionsRecord.class);
    }
}
