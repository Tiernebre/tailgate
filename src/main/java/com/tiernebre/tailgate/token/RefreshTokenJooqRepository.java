package com.tiernebre.tailgate.token;

import java.util.Optional;

public class RefreshTokenJooqRepository implements RefreshTokenRepository {
    @Override
    public RefreshTokenEntity saveOne(RefreshTokenEntity entity) {
        throw new UnsupportedOperationException("Method is not implemented yet.");
    }

    @Override
    public Optional<RefreshTokenEntity> findOneById(String id) {
        throw new UnsupportedOperationException("Method is not implemented yet.");
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
