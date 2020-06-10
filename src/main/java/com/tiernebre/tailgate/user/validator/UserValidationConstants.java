package com.tiernebre.tailgate.user.validator;

public final class UserValidationConstants {
    public static final int NUMBER_OF_ALLOWED_SECURITY_QUESTIONS = 2;
    public static final String NUMBER_OF_SECURITY_QUESTIONS_VALIDATION_MESSAGE =
            "securityQuestions must have exactly " +
                    NUMBER_OF_ALLOWED_SECURITY_QUESTIONS +
                    " entries";
    public static final String NULL_SECURITY_QUESTION_ENTRIES_VALIDATION_MESSAGE =
            "securityQuestions must not include null entries";
    public static final int MINIMUM_PASSWORD_LENGTH = 8;
    public static final int MAXIMUM_PASSWORD_LENGTH = 71;
}
