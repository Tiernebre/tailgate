package com.tiernebre.tailgate.token;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.tiernebre.tailgate.jooq.Tables.REFRESH_TOKENS;

@Repository
@RequiredArgsConstructor
public class RefreshTokenJooqRepository implements RefreshTokenRepository {
    private final DSLContext dslContext;

    @Override
    public RefreshTokenEntity saveOne(RefreshTokenEntity entity) {
        throw new UnsupportedOperationException("Method is not implemented yet.");
    }

    @Override
    public Optional<RefreshTokenEntity> findOneById(String id) {
        return dslContext.select(REFRESH_TOKENS.TOKEN)
                .from(REFRESH_TOKENS)
                .where(REFRESH_TOKENS.TOKEN.eq(id))
                .fetchOptionalInto(RefreshTokenEntity.class);
    }

    @Override
    public Optional<RefreshTokenEntity> updateOne(RefreshTokenEntity entity) {
        throw new UnsupportedOperationException("Method is not implemented yet.");
    }

    @Override
    public Boolean deleteOneById(Long id) {
        throw new UnsupportedOperationException("Method is not implemented yet.");
    }
}
