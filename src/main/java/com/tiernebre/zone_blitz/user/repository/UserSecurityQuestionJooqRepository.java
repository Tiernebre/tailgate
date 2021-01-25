package com.tiernebre.zone_blitz.user.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

import static com.tiernebre.zone_blitz.jooq.Tables.*;

@Repository
@RequiredArgsConstructor
public class UserSecurityQuestionJooqRepository implements UserSecurityQuestionRepository {
    private final DSLContext dslContext;
    private final UserPasswordResetRepositoryUtilities utilities;

    @Override
    public Map<Long, String> getAnswersForEmailAndResetToken(String email, UUID resetToken) {
        return dslContext
                .select(USER_SECURITY_QUESTION.SECURITY_QUESTION_ID, USER_SECURITY_QUESTION.ANSWER)
                .from(USER_SECURITY_QUESTION)
                .join(USER)
                .on(USER_SECURITY_QUESTION.USER_ID.eq(USER.ID))
                .join(PASSWORD_RESET_TOKEN)
                .on(USER.ID.eq(PASSWORD_RESET_TOKEN.USER_ID))
                .where(USER.EMAIL.eq(email))
                .and(PASSWORD_RESET_TOKEN.TOKEN.eq(resetToken))
                .and(utilities.passwordResetTokenIsNotExpired())
                .fetchMap(USER_SECURITY_QUESTION.SECURITY_QUESTION_ID, USER_SECURITY_QUESTION.ANSWER);
    }
}
