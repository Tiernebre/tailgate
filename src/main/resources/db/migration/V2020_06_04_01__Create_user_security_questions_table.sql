CREATE TABLE IF NOT EXISTS user_security_questions (
    user_id BIGINT NOT NULL REFERENCES users (id),
    security_question_id BIGINT NOT NULL references security_questions (id),
    answer TEXT NOT NULL,
    PRIMARY KEY(user_id, security_question_id)
);
