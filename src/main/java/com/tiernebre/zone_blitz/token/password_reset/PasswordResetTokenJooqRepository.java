package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.tiernebre.zone_blitz.jooq.Tables.PASSWORD_RESET_TOKEN;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenJooqRepository implements PasswordResetTokenRepository {
    private final DSLContext dslContext;

    @Override
    public PasswordResetTokenEntity createOneForUser(UserDto user) {
        return dslContext
                .insertInto(PASSWORD_RESET_TOKEN, PASSWORD_RESET_TOKEN.USER_ID)
                .values(user.getId())
                .returningResult(PASSWORD_RESET_TOKEN.asterisk())
                .fetchOne()
                .into(PasswordResetTokenEntity.class);
    }

    @Override
    public void deleteOne(UUID token) {
        dslContext
                .deleteFrom(PASSWORD_RESET_TOKEN)
                .where(PASSWORD_RESET_TOKEN.TOKEN.eq(token))
                .execute();
    }
}
