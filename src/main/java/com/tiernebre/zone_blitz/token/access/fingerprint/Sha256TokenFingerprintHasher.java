package com.tiernebre.zone_blitz.token.access.fingerprint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
public class Sha256TokenFingerprintHasher implements TokenFingerprintHasher {
    private final static String ALGORITHM = "SHA-256";

    @Override
    public String hashFingerprint(String fingerprint) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] fingerprintDigest = digest.digest(fingerprint.getBytes(UTF_8));
            return DatatypeConverter.printHexBinary(fingerprintDigest);
        } catch (NoSuchAlgorithmException e) {
            log.error("Could not find algorithm for hashing JWT Fingerprint.", e);
            throw new AccessTokenFingerprintNotHashedException();
        }
    }
}
