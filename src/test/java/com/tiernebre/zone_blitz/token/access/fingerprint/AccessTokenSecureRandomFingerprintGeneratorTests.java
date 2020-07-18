package com.tiernebre.zone_blitz.token.access.fingerprint;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class AccessTokenSecureRandomFingerprintGeneratorTests {
    private static final AccessTokenSecureRandomFingerprintGenerator accessTokenSecureRandomFingerprintGenerator = new AccessTokenSecureRandomFingerprintGenerator();

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
