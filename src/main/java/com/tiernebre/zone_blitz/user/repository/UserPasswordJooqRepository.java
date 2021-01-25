package com.tiernebre.zone_blitz.user.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.tiernebre.zone_blitz.jooq.Tables.PASSWORD_RESET_TOKEN;
import static com.tiernebre.zone_blitz.jooq.Tables.USER;

@Repository
@RequiredArgsConstructor
public class UserPasswordJooqRepository implements UserPasswordRepository {
    private final DSLContext dslContext;
    private final UserPasswordResetRepositoryUtilities utilities;

    @Override
    public boolean updateOneWithEmailAndNonExpiredResetToken(String password, String email, UUID resetToken) {
        int numberOfUpdated = dslContext
                .update(USER)
                .set(USER.PASSWORD, password)
                .from(PASSWORD_RESET_TOKEN)
                .where(USER.EMAIL.eq(email))
                .and(PASSWORD_RESET_TOKEN.TOKEN.eq(resetToken))
                .and(utilities.passwordResetTokenIsNotExpired())
                .execute();
        return numberOfUpdated == 1;
    }

    @Override
    public boolean updateOneForId(Long id, String password) {
        int numberOfUpdated = dslContext
                .update(USER)
                .set(USER.PASSWORD, password)
                .where(USER.ID.eq(id))
                .execute();
        return numberOfUpdated == 1;
    }

    @Override
    public Optional<String> findOneForId(Long id) {
        return dslContext
                .select(USER.PASSWORD)
                .from(USER)
                .where(USER.ID.eq(id))
                .fetchOptional(USER.PASSWORD);
    }
}
