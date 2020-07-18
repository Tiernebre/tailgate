package com.tiernebre.zone_blitz.token.access;

public interface TokenFingerprintHasher {
    /**
     * Hashes a given fingerprint identifier.
     * @param fingerprint A fingerprint identifier, typically used in conjunction with an access token.
     * @return The hashed version of the fingerprint identifier.
     */
    String hashFingerprint(String fingerprint);
}
