package com.tiernebre.tailgate.token.password_reset;

import com.tiernebre.tailgate.jooq.tables.records.PasswordResetTokensRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.user.UserRecordPool;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static com.tiernebre.tailgate.jooq.Tables.PASSWORD_RESET_TOKENS;
import static com.tiernebre.tailgate.jooq.Tables.USER_CONFIRMATION_TOKENS;


@Component
@RequiredArgsConstructor
public class PasswordResetTokenRecordPool {
    private final UserRecordPool userRecordPool;
    private final DSLContext dslContext;

    public PasswordResetTokensRecord createAndSaveOne() {
        UsersRecord user = userRecordPool.createAndSaveOne();
        return createAndSaveOneForUser(user);
    }

    public PasswordResetTokensRecord createAndSaveOneForUser(UsersRecord user) {
        Record refreshTokensRecord = dslContext.insertInto(PASSWORD_RESET_TOKENS, PASSWORD_RESET_TOKENS.USER_ID)
                .values(user.getId())
                .returningResult(USER_CONFIRMATION_TOKENS.asterisk())
                .fetchOne();
        return refreshTokensRecord.into(PasswordResetTokensRecord.class);
    }

    public PasswordResetTokensRecord getOneById(String id) {
        return dslContext.selectFrom(PASSWORD_RESET_TOKENS).where(USER_CONFIRMATION_TOKENS.TOKEN.eq(id)).fetchOne();
    }
}
