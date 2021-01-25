package com.tiernebre.zone_blitz.token.user_confirmation;

import com.tiernebre.zone_blitz.jooq.tables.records.UserConfirmationTokenRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserRecord;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.tiernebre.zone_blitz.jooq.Tables.USER_CONFIRMATION_TOKEN;


@Component
@RequiredArgsConstructor
public class UserConfirmationTokenRecordPool {
    private final UserRecordPool userRecordPool;
    private final DSLContext dslContext;

    public UserConfirmationTokenRecord createAndSaveOne() {
        UserRecord user = userRecordPool.createAndSaveOne();
        return createAndSaveOneForUser(user);
    }

    public UserConfirmationTokenRecord createAndSaveOneForUser(UserRecord user) {
        Record refreshTokensRecord = dslContext.insertInto(USER_CONFIRMATION_TOKEN, USER_CONFIRMATION_TOKEN.USER_ID)
                .values(user.getId())
                .returningResult(USER_CONFIRMATION_TOKEN.asterisk())
                .fetchOne();
        return refreshTokensRecord.into(UserConfirmationTokenRecord.class);
    }
}
