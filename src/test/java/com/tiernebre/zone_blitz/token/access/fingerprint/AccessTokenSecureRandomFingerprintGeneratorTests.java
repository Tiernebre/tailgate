package com.tiernebre.zone_blitz.token.access.fingerprint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.security.SecureRandom;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class AccessTokenSecureRandomFingerprintGeneratorTests {
    private AccessTokenSecureRandomFingerprintGenerator accessTokenSecureRandomFingerprintGenerator;

    @BeforeEach
    void setup() {
        accessTokenSecureRandomFingerprintGenerator = new AccessTokenSecureRandomFingerprintGenerator(new SecureRandom());
    }

    @Nested
    @DisplayName("generateOne")
    class GenerateOneTests {
        @Test
        @DisplayName("returns the securely randomized generated fingerprint")
        void returnsTheSecurelyRandomizedGeneratedFingerprint() {
            String generatedFingerprint = accessTokenSecureRandomFingerprintGenerator.generateOne();
            String anotherGeneratedFingerprint = accessTokenSecureRandomFingerprintGenerator.generateOne();
            assertTrue(StringUtils.isNotBlank(generatedFingerprint));
            assertTrue(StringUtils.isNotBlank(anotherGeneratedFingerprint));
            assertNotEquals(generatedFingerprint, anotherGeneratedFingerprint);
        }
    }
}
