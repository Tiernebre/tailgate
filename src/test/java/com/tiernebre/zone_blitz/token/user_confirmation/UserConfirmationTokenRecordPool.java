package com.tiernebre.zone_blitz.token.user_confirmation;

import com.tiernebre.zone_blitz.jooq.tables.records.UserConfirmationTokensRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UsersRecord;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.tiernebre.zone_blitz.jooq.Tables.USER_CONFIRMATION_TOKENS;


@Component
@RequiredArgsConstructor
public class UserConfirmationTokenRecordPool {
    private final UserRecordPool userRecordPool;
    private final DSLContext dslContext;

    public UserConfirmationTokensRecord createAndSaveOne() {
        UsersRecord user = userRecordPool.createAndSaveOne();
        return createAndSaveOneForUser(user);
    }

    public UserConfirmationTokensRecord createAndSaveOneForUser(UsersRecord user) {
        Record refreshTokensRecord = dslContext.insertInto(USER_CONFIRMATION_TOKENS, USER_CONFIRMATION_TOKENS.USER_ID)
                .values(user.getId())
                .returningResult(USER_CONFIRMATION_TOKENS.asterisk())
                .fetchOne();
        return refreshTokensRecord.into(UserConfirmationTokensRecord.class);
    }
}
