package com.tiernebre.tailgate.token.user_confirmation;

import com.tiernebre.tailgate.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.tiernebre.tailgate.jooq.Tables.USER_CONFIRMATION_TOKENS;

@Repository
@RequiredArgsConstructor
public class UserConfirmationTokenJooqRepository {
    private final DSLContext dslContext;

    public UserConfirmationTokenEntity createOneForUser(UserDto user) {
        return dslContext
                .insertInto(USER_CONFIRMATION_TOKENS, USER_CONFIRMATION_TOKENS.USER_ID)
                .values(user.getId())
                .returningResult(USER_CONFIRMATION_TOKENS.asterisk())
                .fetchOne()
                .into(UserConfirmationTokenEntity.class);
    }

    public void deleteOne(String token) {
        dslContext
                .deleteFrom(USER_CONFIRMATION_TOKENS)
                .where(USER_CONFIRMATION_TOKENS.TOKEN.eq(token))
                .execute();
    }
}
