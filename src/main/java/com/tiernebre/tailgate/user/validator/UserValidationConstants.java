package com.tiernebre.tailgate.user.validator;

public final class UserValidationConstants {
    public static final int NUMBER_OF_ALLOWED_SECURITY_QUESTIONS = 2;
    public static final String NUMBER_OF_SECURITY_QUESTIONS_VALIDATION_MESSAGE =
            "security questions must have exactly " +
                    NUMBER_OF_ALLOWED_SECURITY_QUESTIONS +
                    " entries";
    public static final String NUMBER_OF_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE =
            "security question answers must have exactly " +
                    NUMBER_OF_ALLOWED_SECURITY_QUESTIONS +
                    " entries";
    public static final String NULL_SECURITY_QUESTION_ENTRIES_VALIDATION_MESSAGE =
            "security questions must not include null entries";
    public static final String NULL_SECURITY_QUESTION_ANSWERS_ENTRIES_VALIDATION_MESSAGE =
            "security question answers must not include null entries";
    public static final String EMPTY_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE =
            "security question answers must not be empty";
    public static final String NULL_SECURITY_QUESTION_ID_VALIDATION_MESSAGE =
            "security question ID must not be null";
    public static final String NULL_SECURITY_QUESTION_ANSWER_VALIDATION_MESSAGE =
            "security question answer must not be a blank string or null";
    public static final String SAME_SECURITY_QUESTIONS_VALIDATION_MESSAGE =
            "duplicate security questions cannot be chosen, please ensure each of your chosen security questions is unique";
    public static final String SAME_SECURITY_QUESTION_ANSWERS_VALIDATION_MESSAGE =
            "security questions must not have duplicated answers, please ensure each security question has a unique answer";
    public static final String SECURITY_QUESTION_ANSWERS_CANNOT_DUPLICATE_SENSITIVE_INFORMATION =
            "security question answers must not be the same as other user provided information";

    public static final int MINIMUM_PASSWORD_LENGTH = 8;
    public static final int MAXIMUM_PASSWORD_LENGTH = 71;
    public static final String PASSWORD_MATCHES_ERROR = "password and confirmationPassword must equal each other";
    public static final String PASSWORD_CONTAIN_DIGITS_ERROR = "password must contain numerical digits (0-9)";
    public static final String PASSWORD_MIXED_CHARACTERS_ERROR = "password must contain mixed uppercase and lowercase alphabetical characters (A-Z, a-z)";
    public static final String PASSWORD_SPECIAL_CHARACTERS_ERROR = "password must contain non-alphanumeric characters";
    public static final String NULL_PASSWORD_UPDATE_REQUEST_ERROR = "The update password request to update must not be null";
}
