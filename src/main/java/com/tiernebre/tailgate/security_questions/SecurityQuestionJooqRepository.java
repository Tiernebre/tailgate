package com.tiernebre.tailgate.security_questions;

import lombok.RequiredArgsConstructor;
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
        return false;
    }
}
