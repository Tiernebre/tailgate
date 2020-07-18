package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.jooq.tables.records.PasswordResetTokensRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UsersRecord;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.tiernebre.zone_blitz.jooq.Tables.PASSWORD_RESET_TOKENS;


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

    public PasswordResetTokensRecord getOneById(UUID id) {
        return dslContext.selectFrom(PASSWORD_RESET_TOKENS).where(PASSWORD_RESET_TOKENS.TOKEN.eq(id)).fetchOne();
    }

    public void deleteAll() {
        dslContext.deleteFrom(PASSWORD_RESET_TOKENS).execute();
    }
}
