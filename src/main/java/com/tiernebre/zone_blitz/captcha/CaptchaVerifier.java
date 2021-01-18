package com.tiernebre.zone_blitz.captcha;

public interface CaptchaVerifier {
    /**
     * Verifies that a given captcha token is legitimate.
     *
     * If the captcha token is valid, the function won't throw any errors.
     * If the token is invalid, then an error will get thrown.
     *
     * @param captchaToken The token to verify.
     * @throws CaptchaIsNotValidException If the token provided was invalid / deemed to be fraudulent.
     */
    void verify(String captchaToken) throws CaptchaIsNotValidException;
}
