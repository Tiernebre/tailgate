package com.tiernebre.zone_blitz.token.refresh;

import com.tiernebre.zone_blitz.jooq.tables.records.RefreshTokenRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserRecord;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.tiernebre.zone_blitz.jooq.Tables.REFRESH_TOKEN;

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

    public RefreshTokenRecord createAndSaveOne() {
        UserRecord user = userRecordPool.createAndSaveOne();
        return createAndSaveOneForUser(user);
    }

    public RefreshTokenRecord createAndSaveOneForUser(UserRecord user) {
        Record refreshTokensRecord = dslContext.insertInto(REFRESH_TOKEN, REFRESH_TOKEN.USER_ID)
                .values(user.getId())
                .returningResult(REFRESH_TOKEN.asterisk())
                .fetchOne();
        return refreshTokensRecord.into(RefreshTokenRecord.class);
    }

    public RefreshTokenRecord getOneById(UUID id) {
        return dslContext.selectFrom(REFRESH_TOKEN).where(REFRESH_TOKEN.TOKEN.eq(id)).fetchOne();
    }
}
