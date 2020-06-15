package com.tiernebre.tailgate.user.repository;

import com.tiernebre.tailgate.token.password_reset.PasswordResetTokenConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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
                .and(
                        localDateTimeAdd(
                                PASSWORD_RESET_TOKENS.CREATED_AT,
                                configurationProperties.getExpirationWindowInMinutes(),
                                DatePart.MINUTE
                        ).greaterThan(LocalDateTime.now()))
                .execute();
        return numberOfUpdated == 1;
    }
}
