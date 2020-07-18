package com.tiernebre.zone_blitz.token.access.fingerprint;

public class AccessTokenFingerprintNotHashedException extends RuntimeException {
    public AccessTokenFingerprintNotHashedException() {
        super("The access token fingerprint could not be properly hashed.");
    }
}
