package com.tiernebre.zone_blitz.token.access.fingerprint;

import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

@Component
public class AccessTokenSecureRandomFingerprintGenerator implements AccessTokenFingerprintGenerator {
    private final static int SIZE_OF_FINGERPRINT_IN_BYTES = 50;

    @Override
    public String generateOne() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomFingerprint = new byte[SIZE_OF_FINGERPRINT_IN_BYTES];
        secureRandom.nextBytes(randomFingerprint);
        return DatatypeConverter.printHexBinary(randomFingerprint);
    }
}
