package com.tiernebre.tailgate.security_questions;

import com.tiernebre.tailgate.jooq.tables.records.SecurityQuestionsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.tiernebre.tailgate.jooq.tables.SecurityQuestions.SECURITY_QUESTIONS;

@Component
@RequiredArgsConstructor
public class SecurityQuestionRecordPool {
    private final DSLContext dslContext;

    public List<SecurityQuestionsRecord> createMultiple() {
        List<SecurityQuestionsRecord> securityQuestionsRecords = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SecurityQuestionsRecord securityQuestionsRecord = dslContext.newRecord(SECURITY_QUESTIONS);
            securityQuestionsRecord.setQuestion(UUID.randomUUID().toString());
            securityQuestionsRecord.store();
            securityQuestionsRecords.add(securityQuestionsRecord);
        }
        return securityQuestionsRecords;
    }
}
