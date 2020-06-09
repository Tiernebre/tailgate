package com.tiernebre.tailgate.security_questions;

import java.util.List;
import java.util.Set;

public interface SecurityQuestionService {
    /**
     * Returns all of the security questions
     * @return all of the security questions
     */
    List<SecurityQuestionDto> getAll();

    /**
     * Determines if a set with some ids do not exist.
     * @param ids The ids to check
     * @return false if _all_ ids match up to security questions
     *         true if at least one of the ids does not match up to a security question
     */
    boolean someDoNotExistWithIds(Set<Long> ids);
}
