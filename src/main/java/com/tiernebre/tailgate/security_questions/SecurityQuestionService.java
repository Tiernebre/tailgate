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
     * Determines if a set of ids provided all exist in a data store.
     * @param ids The ids to check
     * @return true if _all_ ids match up to security questions
     *         false if at least one of the ids does not match up to a security question
     */
    boolean allExistWithIds(Set<Long> ids);
}
