package com.tiernebre.tailgate.security_questions;

import java.util.List;

public interface SecurityQuestionRepository {
    /**
     * Retrieves all of the security questions.
     * @return All of the security questions.
     */
    List<SecurityQuestionEntity> getAll();
}
