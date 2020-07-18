package com.tiernebre.zone_blitz.token.access;

import org.springframework.stereotype.Component;

@Component
public class Sha256TokenFingerprintHasher implements TokenFingerprintHasher {
    @Override
    public String hashFingerprint(String fingerprint) {
        return null;
    }
}
