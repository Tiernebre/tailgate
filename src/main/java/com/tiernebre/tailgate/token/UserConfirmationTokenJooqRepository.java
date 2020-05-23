package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.tiernebre.tailgate.jooq.Tables.INVITE_TOKENS;

@Repository
@RequiredArgsConstructor
public class UserConfirmationTokenJooqRepository {
    private final DSLContext dslContext;

    public UserConfirmationTokenEntity createOneForUser(UserDto user) {
        return dslContext
                .insertInto(INVITE_TOKENS, INVITE_TOKENS.USER_ID)
                .values(user.getId())
                .returningResult(INVITE_TOKENS.asterisk())
                .fetchOne()
                .into(UserConfirmationTokenEntity.class);
    }

    public void deleteOne(String token) {
        dslContext
                .deleteFrom(INVITE_TOKENS)
                .where(INVITE_TOKENS.TOKEN.eq(token))
                .execute();
    }
}
