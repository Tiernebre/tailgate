package com.tiernebre.tailgate.user.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.tiernebre.tailgate.jooq.tables.PasswordResetTokens.PASSWORD_RESET_TOKENS;
import static com.tiernebre.tailgate.jooq.tables.Users.USERS;

@Repository
@RequiredArgsConstructor
public class UserPasswordJooqRepository implements UserPasswordRepository {
    private final DSLContext dslContext;

    @Override
    public void updateOneWithEmailAndNonExpiredResetToken(String password, String email, String resetToken) {
        dslContext
                .update(USERS)
                .set(USERS.PASSWORD, password)
                .from(PASSWORD_RESET_TOKENS)
                .where(USERS.EMAIL.eq(email))
                .and(PASSWORD_RESET_TOKENS.TOKEN.eq(resetToken))
                .execute();
    }
}
