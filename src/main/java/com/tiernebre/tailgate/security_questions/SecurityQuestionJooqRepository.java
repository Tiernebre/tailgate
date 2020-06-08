package com.tiernebre.tailgate.security_questions;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

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

    private int getCountForAllWithIds(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return 0;

        return dslContext.fetchCount(
                dslContext.selectFrom(SECURITY_QUESTIONS)
                        .where(SECURITY_QUESTIONS.ID.in(ids))
        );
    }
}
