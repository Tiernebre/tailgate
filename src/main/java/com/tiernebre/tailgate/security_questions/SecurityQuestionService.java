package com.tiernebre.tailgate.security_questions;

import java.util.List;

public interface SecurityQuestionService {
    /**
     * Returns all of the security questions
     * @return all of the security questions
     */
    List<SecurityQuestionDto> getAll();
}
