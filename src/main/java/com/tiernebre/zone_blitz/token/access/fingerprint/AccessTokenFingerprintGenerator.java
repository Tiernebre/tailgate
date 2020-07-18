package com.tiernebre.zone_blitz.token.access.fingerprint;

public interface AccessTokenFingerprintGenerator {
    /**
     * Generates an access token fingerprint.
     * @return The generated access token fingerprint.
     */
    String generateOne();
}
