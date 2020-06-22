package com.tiernebre.tailgate.token.user_confirmation;

import com.tiernebre.tailgate.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.tiernebre.tailgate.jooq.Tables.USER_CONFIRMATION_TOKENS;

@Repository
@RequiredArgsConstructor
public class UserConfirmationTokenJooqRepository implements UserConfirmationTokenRepository {
    private final DSLContext dslContext;

    @Override
    public UserConfirmationTokenEntity createOneForUser(UserDto user) {
        return dslContext
                .insertInto(USER_CONFIRMATION_TOKENS, USER_CONFIRMATION_TOKENS.USER_ID)
                .values(user.getId())
                .returningResult(USER_CONFIRMATION_TOKENS.asterisk())
                .fetchOne()
                .into(UserConfirmationTokenEntity.class);
    }

    @Override
    public void deleteOne(String token) {
        dslContext
                .deleteFrom(USER_CONFIRMATION_TOKENS)
                .where(USER_CONFIRMATION_TOKENS.TOKEN.eq(token))
                .execute();
    }

    @Override
    public Optional<UserConfirmationTokenEntity> findOneForUser(UserDto user) {
        return dslContext
                .selectFrom(USER_CONFIRMATION_TOKENS)
                .where(USER_CONFIRMATION_TOKENS.USER_ID.eq(user.getId()))
                .fetchOptionalInto(UserConfirmationTokenEntity.class);
    }
}
