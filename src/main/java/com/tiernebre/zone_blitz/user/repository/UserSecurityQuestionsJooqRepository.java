package com.tiernebre.zone_blitz.user.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

import static com.tiernebre.zone_blitz.jooq.Tables.USER_SECURITY_QUESTIONS;
import static com.tiernebre.zone_blitz.jooq.tables.PasswordResetTokens.PASSWORD_RESET_TOKENS;
import static com.tiernebre.zone_blitz.jooq.tables.Users.USERS;

@Repository
@RequiredArgsConstructor
public class UserSecurityQuestionsJooqRepository implements UserSecurityQuestionsRepository {
    private final DSLContext dslContext;
    private final UserPasswordResetRepositoryUtilities utilities;

    @Override
    public Map<Long, String> getAnswersForEmailAndResetToken(String email, UUID resetToken) {
        return dslContext
                .select(USER_SECURITY_QUESTIONS.SECURITY_QUESTION_ID, USER_SECURITY_QUESTIONS.ANSWER)
                .from(USER_SECURITY_QUESTIONS)
                .join(USERS)
                .on(USER_SECURITY_QUESTIONS.USER_ID.eq(USERS.ID))
                .join(PASSWORD_RESET_TOKENS)
                .on(USERS.ID.eq(PASSWORD_RESET_TOKENS.USER_ID))
                .where(USERS.EMAIL.eq(email))
                .and(utilities.passwordResetTokenIsNotExpired())
                .fetchMap(USER_SECURITY_QUESTIONS.SECURITY_QUESTION_ID, USER_SECURITY_QUESTIONS.ANSWER);
    }
}