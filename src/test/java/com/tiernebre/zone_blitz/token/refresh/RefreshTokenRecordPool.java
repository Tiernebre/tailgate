package com.tiernebre.zone_blitz.token.refresh;

import com.tiernebre.zone_blitz.jooq.tables.records.RefreshTokensRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UsersRecord;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.tiernebre.zone_blitz.jooq.Tables.REFRESH_TOKENS;

/**
 * Manages data within the jooq data context that is used within the integration tests.
 *
 * Entirely used for scenarios where we want to avoid the repository equivalent of doing something, as this
 * lets us test a single individual method at a time (leading to less noisy test errors if one repository method
 * breaks).
 */
@Component
@RequiredArgsConstructor
public class RefreshTokenRecordPool {
    private final UserRecordPool userRecordPool;
    private final DSLContext dslContext;

    public RefreshTokensRecord createAndSaveOne() {
        UsersRecord user = userRecordPool.createAndSaveOne();
        return createAndSaveOneForUser(user);
    }

    public RefreshTokensRecord createAndSaveOneForUser(UsersRecord user) {
        Record refreshTokensRecord = dslContext.insertInto(REFRESH_TOKENS, REFRESH_TOKENS.USER_ID)
                .values(user.getId())
                .returningResult(REFRESH_TOKENS.asterisk())
                .fetchOne();
        return refreshTokensRecord.into(RefreshTokensRecord.class);
    }

    public RefreshTokensRecord getOneById(String id) {
        return dslContext.selectFrom(REFRESH_TOKENS).where(REFRESH_TOKENS.TOKEN.eq(id)).fetchOne();
    }
}
