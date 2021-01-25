package com.tiernebre.zone_blitz.token.password_reset;

import com.tiernebre.zone_blitz.jooq.tables.records.PasswordResetTokenRecord;
import com.tiernebre.zone_blitz.jooq.tables.records.UserRecord;
import com.tiernebre.zone_blitz.user.UserRecordPool;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.tiernebre.zone_blitz.jooq.Tables.PASSWORD_RESET_TOKEN;


@Component
@RequiredArgsConstructor
public class PasswordResetTokenRecordPool {
    private final UserRecordPool userRecordPool;
    private final DSLContext dslContext;

    public PasswordResetTokenRecord createAndSaveOne() {
        UserRecord user = userRecordPool.createAndSaveOne();
        return createAndSaveOneForUser(user);
    }

    public PasswordResetTokenRecord createAndSaveOneForUser(UserRecord user) {
        PasswordResetTokenRecord passwordResetTokensRecord = dslContext.newRecord(PASSWORD_RESET_TOKEN);
        passwordResetTokensRecord.setUserId(user.getId());
        passwordResetTokensRecord.store();
        return passwordResetTokensRecord.into(PasswordResetTokenRecord.class);
    }

    public PasswordResetTokenRecord getOneById(UUID id) {
        return dslContext.selectFrom(PASSWORD_RESET_TOKEN).where(PASSWORD_RESET_TOKEN.TOKEN.eq(id)).fetchOne();
    }
}
