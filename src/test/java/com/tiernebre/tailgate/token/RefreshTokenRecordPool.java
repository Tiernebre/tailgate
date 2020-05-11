package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.jooq.tables.records.RefreshTokensRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.user.UserRecordPool;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.tiernebre.tailgate.jooq.Tables.REFRESH_TOKENS;

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
        UsersRecord createdUser = userRecordPool.createAndSaveOne();
        Record refreshTokensRecord = dslContext.insertInto(REFRESH_TOKENS, REFRESH_TOKENS.USER_ID)
                .values(createdUser.getId())
                .returningResult(REFRESH_TOKENS.asterisk())
                .fetchOne();
        return refreshTokensRecord.into(RefreshTokensRecord.class);
    }
}
