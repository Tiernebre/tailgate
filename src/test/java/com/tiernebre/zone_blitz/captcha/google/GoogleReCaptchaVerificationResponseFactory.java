package com.tiernebre.zone_blitz.captcha.google;

import com.tiernebre.zone_blitz.captcha.google.GoogleReCaptchaVerificationResponse;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.UUID;

public class GoogleReCaptchaVerificationResponseFactory {
    public static GoogleReCaptchaVerificationResponse generateOneDto() {
        return GoogleReCaptchaVerificationResponse.builder()
                .success(true)
                .challengeTimestamp(ZonedDateTime.now())
                .hostname(UUID.randomUUID().toString())
                .errorCodes(Collections.emptySet())
                .build();
    }
}
