package com.tiernebre.tailgate.security_questions;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class SecurityQuestionJooqRepository implements SecurityQuestionRepository {
    @Override
    public List<SecurityQuestionEntity> getAll() {
        return null;
    }
}
