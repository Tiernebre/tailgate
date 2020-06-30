package com.tiernebre.zone_blitz.token.refresh;

import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.validator.StringValidator;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.tiernebre.zone_blitz.jooq.Tables.REFRESH_TOKENS;
import static com.tiernebre.zone_blitz.token.refresh.RefreshTokenConstants.BLANK_TOKEN_ERROR_MESSAGE;
import static com.tiernebre.zone_blitz.token.refresh.RefreshTokenConstants.NULL_USER_ERROR_MESSAGE;

@Repository
@RequiredArgsConstructor
public class RefreshTokenJooqRepository implements RefreshTokenRepository {
    private final DSLContext dslContext;

    @Override
    public RefreshTokenEntity createOneForUser(UserDto user) {
        Objects.requireNonNull(user, NULL_USER_ERROR_MESSAGE);

        return dslContext
                .insertInto(REFRESH_TOKENS, REFRESH_TOKENS.USER_ID)
                .values(user.getId())
                .returningResult(REFRESH_TOKENS.asterisk())
                .fetchOne()
                .into(RefreshTokenEntity.class);
   }

    @Override
    public void deleteOne(String token) {
        StringValidator.requireNonBlank(token, BLANK_TOKEN_ERROR_MESSAGE);

        dslContext
                .deleteFrom(REFRESH_TOKENS)
                .where(REFRESH_TOKENS.TOKEN.eq(token))
                .execute();
    }
}
