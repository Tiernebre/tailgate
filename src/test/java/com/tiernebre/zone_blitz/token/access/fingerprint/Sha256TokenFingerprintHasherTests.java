package com.tiernebre.zone_blitz.token.access.fingerprint;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class Sha256TokenFingerprintHasherTests {
    @InjectMocks
    private Sha256TokenFingerprintHasher sha256TokenFingerprintHasher;

    @Nested
    @DisplayName("hashFingerprint")
    class HashFingerprintTests {
        @Test
        @DisplayName("returns the SHA-256 hashed version of the provided fingerprint")
        void returnsTheSha256HashedVersionOfTheProvidedFingerprint() {
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomFgp = new byte[50];
            secureRandom.nextBytes(randomFgp);
            String fingerprint = DatatypeConverter.printHexBinary(randomFgp);
            String hashedFingerprint = sha256TokenFingerprintHasher.hashFingerprint(fingerprint);
            assertNotNull(hashedFingerprint);
            assertNotEquals(fingerprint, hashedFingerprint);
        }
    }
}
