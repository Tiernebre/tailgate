package com.tiernebre.zone_blitz.token.access.fingerprint;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

import static org.junit.Assert.*;

@ExtendWith(MockitoExtension.class)
public class Sha256AccessTokenFingerprintHasherTests {
    @InjectMocks
    private Sha256AccessTokenFingerprintHasher sha256TokenFingerprintHasher;

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

        @EmptySource
        @NullSource
        @ValueSource(strings = { " ", "" })
        @ParameterizedTest(name = "returns \"{0}\" if given \"{0}\"")
        void returnsOriginalStringIfGiven(String blankString) {
            assertEquals(blankString, sha256TokenFingerprintHasher.hashFingerprint(blankString));
        }
    }
}
