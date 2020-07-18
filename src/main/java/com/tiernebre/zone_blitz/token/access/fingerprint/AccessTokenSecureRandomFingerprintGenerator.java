package com.tiernebre.zone_blitz.token.access.fingerprint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class AccessTokenSecureRandomFingerprintGenerator implements AccessTokenFingerprintGenerator {
    private final static int SIZE_OF_FINGERPRINT_IN_BYTES = 50;
    private final SecureRandom secureRandom;

    @Override
    public String generateOne() {
        byte[] randomFingerprint = new byte[SIZE_OF_FINGERPRINT_IN_BYTES];
        secureRandom.nextBytes(randomFingerprint);
        return DatatypeConverter.printHexBinary(randomFingerprint);
    }
}
