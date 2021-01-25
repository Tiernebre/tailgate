package com.tiernebre.zone_blitz.security_questions;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.tiernebre.zone_blitz.jooq.Tables.*;

@Repository
@Primary
@RequiredArgsConstructor
public class SecurityQuestionJooqRepository implements SecurityQuestionRepository {
    private final DSLContext dslContext;

    @Override
    public List<SecurityQuestionEntity> getAll() {
        return dslContext
                .selectFrom(SECURITY_QUESTION)
                .fetchInto(SecurityQuestionEntity.class);
    }

    @Override
    public boolean allExistWithIds(Set<Long> ids) {
        int numberOfFoundSecurityQuestion = getCountForAllWithIds(ids);
        return numberOfFoundSecurityQuestion > 0 && numberOfFoundSecurityQuestion == ids.size();
    }

    @Override
    public List<SecurityQuestionEntity> getAllForPasswordResetToken(UUID passwordResetToken) {
        return dslContext
                .select(SECURITY_QUESTION.asterisk())
                .from(SECURITY_QUESTION)
                .join(USER_SECURITY_QUESTION)
                .on(SECURITY_QUESTION.ID.eq(USER_SECURITY_QUESTION.SECURITY_QUESTION_ID))
                .join(USER)
                .on(USER_SECURITY_QUESTION.USER_ID.eq(USER.ID))
                .join(PASSWORD_RESET_TOKEN)
                .on(PASSWORD_RESET_TOKEN.USER_ID.eq(USER.ID))
                .where(PASSWORD_RESET_TOKEN.TOKEN.eq(passwordResetToken))
                .fetchInto(SecurityQuestionEntity.class);
    }

    private int getCountForAllWithIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return 0;

        return dslContext.fetchCount(
                dslContext.selectFrom(SECURITY_QUESTION)
                        .where(SECURITY_QUESTION.ID.in(ids))
        );
    }
}
