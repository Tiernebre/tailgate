package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.jooq.tables.records.RefreshTokensRecord;
import com.tiernebre.tailgate.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.tiernebre.tailgate.jooq.Tables.REFRESH_TOKENS;

@Repository
@RequiredArgsConstructor
public class RefreshTokenJooqRepository implements RefreshTokenRepository {
    private final DSLContext dslContext;

    @Override
    public RefreshTokenEntity createOneForUser(UserDto user) {
        return dslContext.insertInto(REFRESH_TOKENS, REFRESH_TOKENS.USER_ID)
                .values(user.getId())
                .returningResult(REFRESH_TOKENS.asterisk())
                .fetchOne()
                .into(RefreshTokenEntity.class);
    }

    @Override
    public Optional<RefreshTokenEntity> findOneById(String id) {
        return dslContext.selectFrom(REFRESH_TOKENS)
                .where(REFRESH_TOKENS.TOKEN.eq(id))
                .fetchOptionalInto(RefreshTokenEntity.class);
    }
}
