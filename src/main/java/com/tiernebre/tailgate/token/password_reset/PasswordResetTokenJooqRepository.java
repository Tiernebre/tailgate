package com.tiernebre.tailgate.token.password_reset;

import com.tiernebre.tailgate.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.tiernebre.tailgate.jooq.Tables.PASSWORD_RESET_TOKENS;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenJooqRepository implements PasswordResetTokenRepository {
    private final DSLContext dslContext;

    @Override
    public PasswordResetTokenEntity createOneForUser(UserDto user) {
        return dslContext
                .insertInto(PASSWORD_RESET_TOKENS, PASSWORD_RESET_TOKENS.USER_ID)
                .values(user.getId())
                .returningResult(PASSWORD_RESET_TOKENS.asterisk())
                .fetchOne()
                .into(PasswordResetTokenEntity.class);
    }

    @Override
    public void deleteOne(String token) {
        dslContext
                .deleteFrom(PASSWORD_RESET_TOKENS)
                .where(PASSWORD_RESET_TOKENS.TOKEN.eq(token))
                .execute();
    }
}
