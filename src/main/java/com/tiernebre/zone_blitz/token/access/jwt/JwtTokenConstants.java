package com.tiernebre.zone_blitz.token.access.jwt;

class JwtTokenConstants {
    static final String ISSUER = "zone-blitz";
    static final String EMAIL_CLAIM = "email";
    static final String IS_CONFIRMED_CLAIM = "isConfirmed";
    static final String NULL_USER_ERROR_MESSAGE = "The user to generate a JWT token from must not be null.";
    static final String FINGERPRINT_CLAIM = "userFingerprint";
}
