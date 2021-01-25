package com.tiernebre.zone_blitz.token.user_confirmation;

import com.tiernebre.zone_blitz.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.tiernebre.zone_blitz.jooq.Tables.USER_CONFIRMATION_TOKEN;

@Repository
@RequiredArgsConstructor
public class UserConfirmationTokenJooqRepository implements UserConfirmationTokenRepository {
    private final DSLContext dslContext;

    @Override
    public UserConfirmationTokenEntity createOneForUser(UserDto user) {
        return dslContext
                .insertInto(USER_CONFIRMATION_TOKEN, USER_CONFIRMATION_TOKEN.USER_ID)
                .values(user.getId())
                .returningResult(USER_CONFIRMATION_TOKEN.asterisk())
                .fetchOne()
                .into(UserConfirmationTokenEntity.class);
    }

    @Override
    public Optional<UserConfirmationTokenEntity> findOneForUser(UserDto user) {
        return dslContext
                .selectFrom(USER_CONFIRMATION_TOKEN)
                .where(USER_CONFIRMATION_TOKEN.USER_ID.eq(user.getId()))
                .fetchOptionalInto(UserConfirmationTokenEntity.class);
    }
}
