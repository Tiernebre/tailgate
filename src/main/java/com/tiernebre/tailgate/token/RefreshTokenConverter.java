package com.tiernebre.tailgate.token;

public interface RefreshTokenConverter {
    /**
     * Converts a refresh token to its DTO equivalent.
     * @param refreshTokenEntity The entity to convert to a DTO.
     * @return The DTO mapped refresh token.
     */
    RefreshTokenDto convertToDto(RefreshTokenEntity refreshTokenEntity);
}
