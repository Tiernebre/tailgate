package com.tiernebre.tailgate.user.repository;

import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;

import static com.tiernebre.tailgate.jooq.Tables.USER_SECURITY_QUESTIONS;
import static com.tiernebre.tailgate.jooq.tables.PasswordResetTokens.PASSWORD_RESET_TOKENS;
import static com.tiernebre.tailgate.jooq.tables.Users.USERS;
import static org.jooq.impl.DSL.localDateTimeAdd;

@Repository
@RequiredArgsConstructor
public class UserPasswordJooqRepository implements UserPasswordRepository {
    private final DSLContext dslContext;
    private final PasswordResetTokenConfigurationProperties configurationProperties;

    @Override
    public boolean updateOneWithEmailAndNonExpiredResetToken(String password, String email, String resetToken) {
        int numberOfUpdated = dslContext
                .update(USERS)
                .set(USERS.PASSWORD, password)
                .from(PASSWORD_RESET_TOKENS)
                .where(USERS.EMAIL.eq(email))
                .and(PASSWORD_RESET_TOKENS.TOKEN.eq(resetToken))
                .and(passwordResetTokenIsNotExpired())
                .execute();
        return numberOfUpdated == 1;
    }

    @Override
    public Map<Long, String> getSecurityQuestionAnswersForEmailAndNonExpiredResetToken(String email, String resetToken) {
        return dslContext
                .select(USER_SECURITY_QUESTIONS.SECURITY_QUESTION_ID, USER_SECURITY_QUESTIONS.ANSWER)
                .from(USER_SECURITY_QUESTIONS)
                .join(USERS)
                .on(USER_SECURITY_QUESTIONS.USER_ID.eq(USERS.ID))
                .join(PASSWORD_RESET_TOKENS)
                .on(USERS.ID.eq(PASSWORD_RESET_TOKENS.USER_ID))
                .where(USERS.EMAIL.eq(email))
                .and(passwordResetTokenIsNotExpired())
                .fetchMap(USER_SECURITY_QUESTIONS.SECURITY_QUESTION_ID, USER_SECURITY_QUESTIONS.ANSWER);
    }

    private Condition passwordResetTokenIsNotExpired() {
        return localDateTimeAdd(
                PASSWORD_RESET_TOKENS.CREATED_AT,
                configurationProperties.getExpirationWindowInMinutes(),
                DatePart.MINUTE
        ).greaterThan(LocalDateTime.now());
    }
}
