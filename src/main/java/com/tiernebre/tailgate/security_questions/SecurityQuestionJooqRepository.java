package com.tiernebre.tailgate.security_questions;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static com.tiernebre.tailgate.jooq.Tables.*;
import static com.tiernebre.tailgate.jooq.tables.SecurityQuestions.SECURITY_QUESTIONS;

@Repository
@Primary
@RequiredArgsConstructor
public class SecurityQuestionJooqRepository implements SecurityQuestionRepository {
    private final DSLContext dslContext;

    @Override
    public List<SecurityQuestionEntity> getAll() {
        return dslContext
                .selectFrom(SECURITY_QUESTIONS)
                .fetchInto(SecurityQuestionEntity.class);
    }

    @Override
    public boolean allExistWithIds(Set<Long> ids) {
        int numberOfFoundSecurityQuestions = getCountForAllWithIds(ids);
        return numberOfFoundSecurityQuestions > 0 && numberOfFoundSecurityQuestions == ids.size();
    }

    @Override
    public List<SecurityQuestionEntity> getAllForPasswordResetToken(String passwordResetToken) {
        return dslContext
                .select(SECURITY_QUESTIONS.asterisk())
                .from(SECURITY_QUESTIONS)
                .join(USER_SECURITY_QUESTIONS)
                .on(SECURITY_QUESTIONS.ID.eq(USER_SECURITY_QUESTIONS.SECURITY_QUESTION_ID))
                .join(USERS)
                .on(USER_SECURITY_QUESTIONS.USER_ID.eq(USERS.ID))
                .join(PASSWORD_RESET_TOKENS)
                .on(PASSWORD_RESET_TOKENS.USER_ID.eq(USERS.ID))
                .where(PASSWORD_RESET_TOKENS.TOKEN.eq(passwordResetToken))
                .fetchInto(SecurityQuestionEntity.class);
    }

    private int getCountForAllWithIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return 0;

        return dslContext.fetchCount(
                dslContext.selectFrom(SECURITY_QUESTIONS)
                        .where(SECURITY_QUESTIONS.ID.in(ids))
        );
    }
}
