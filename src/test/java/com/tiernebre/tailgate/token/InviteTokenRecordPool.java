package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.jooq.tables.records.InviteTokensRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.user.UserRecordPool;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.tiernebre.tailgate.jooq.Tables.INVITE_TOKENS;

@Component
@RequiredArgsConstructor
public class InviteTokenRecordPool {
    private final UserRecordPool userRecordPool;
    private final DSLContext dslContext;

    public InviteTokensRecord createAndSaveOne() {
        UsersRecord user = userRecordPool.createAndSaveOne();
        return createAndSaveOneForUser(user);
    }

    public InviteTokensRecord createAndSaveOneForUser(UsersRecord user) {
        Record refreshTokensRecord = dslContext.insertInto(INVITE_TOKENS, INVITE_TOKENS.USER_ID)
                .values(user.getId())
                .returningResult(INVITE_TOKENS.asterisk())
                .fetchOne();
        return refreshTokensRecord.into(InviteTokensRecord.class);
    }

    public InviteTokensRecord getOneById(String id) {
        return dslContext.selectFrom(INVITE_TOKENS).where(INVITE_TOKENS.TOKEN.eq(id)).fetchOne();
    }
}
