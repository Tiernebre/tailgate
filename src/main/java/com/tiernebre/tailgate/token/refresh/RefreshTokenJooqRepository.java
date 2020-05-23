package com.tiernebre.tailgate.token.refresh;

import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.validator.StringValidator;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Objects;

import static com.tiernebre.tailgate.jooq.Tables.REFRESH_TOKENS;

@Repository
@RequiredArgsConstructor
public class RefreshTokenJooqRepository implements RefreshTokenRepository {
    static final String NULL_USER_ERROR_MESSAGE = "The user to create a refresh token for must not be null.";
    static final String BLANK_TOKEN_ERROR_MESSAGE = "The token provided must be a non-null and non-blank string";

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
