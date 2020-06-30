package com.tiernebre.zone_blitz.user.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.tiernebre.zone_blitz.jooq.tables.PasswordResetTokens.PASSWORD_RESET_TOKENS;
import static com.tiernebre.zone_blitz.jooq.tables.Users.USERS;

@Repository
@RequiredArgsConstructor
public class UserPasswordJooqRepository implements UserPasswordRepository {
    private final DSLContext dslContext;
    private final UserPasswordResetRepositoryUtilities utilities;

    @Override
    public boolean updateOneWithEmailAndNonExpiredResetToken(String password, String email, String resetToken) {
        int numberOfUpdated = dslContext
                .update(USERS)
                .set(USERS.PASSWORD, password)
                .from(PASSWORD_RESET_TOKENS)
                .where(USERS.EMAIL.eq(email))
                .and(PASSWORD_RESET_TOKENS.TOKEN.eq(resetToken))
                .and(utilities.passwordResetTokenIsNotExpired())
                .execute();
        return numberOfUpdated == 1;
    }

    @Override
    public boolean updateOneForId(Long id, String password) {
        int numberOfUpdated = dslContext
                .update(USERS)
                .set(USERS.PASSWORD, password)
                .where(USERS.ID.eq(id))
                .execute();
        return numberOfUpdated == 1;
    }

    @Override
    public Optional<String> findOneForId(Long id) {
        return dslContext
                .select(USERS.PASSWORD)
                .from(USERS)
                .where(USERS.ID.eq(id))
                .fetchOptional(USERS.PASSWORD);
    }
}
