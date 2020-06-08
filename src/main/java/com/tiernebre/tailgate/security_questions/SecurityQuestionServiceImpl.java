package com.tiernebre.tailgate.security_questions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SecurityQuestionServiceImpl implements SecurityQuestionService {
    private final SecurityQuestionConverter converter;
    private final SecurityQuestionRepository repository;

    @Override
    public List<SecurityQuestionDto> getAll() {
        return converter.createFromEntities(repository.getAll());
    }

    @Override
    public boolean allExistWithIds(Set<Long> ids) {
        return repository.allExistWithIds(ids);
    }
}
