package com.tiernebre.tailgate.token.password_reset;

import com.tiernebre.tailgate.jooq.tables.records.PasswordResetTokensRecord;
import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.user.UserRecordPool;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import static com.tiernebre.tailgate.jooq.Tables.PASSWORD_RESET_TOKENS;


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
        PasswordResetTokensRecord passwordResetTokensRecord = dslContext.newRecord(PASSWORD_RESET_TOKENS);
        passwordResetTokensRecord.setUserId(user.getId());
        passwordResetTokensRecord.store();
        return passwordResetTokensRecord.into(PasswordResetTokensRecord.class);
    }

    public PasswordResetTokensRecord getOneById(String id) {
        return dslContext.selectFrom(PASSWORD_RESET_TOKENS).where(PASSWORD_RESET_TOKENS.TOKEN.eq(id)).fetchOne();
    }

    public void deleteAll() {
        dslContext.deleteFrom(PASSWORD_RESET_TOKENS).execute();
    }
}
