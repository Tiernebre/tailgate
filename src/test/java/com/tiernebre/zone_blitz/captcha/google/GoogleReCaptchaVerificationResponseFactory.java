package com.tiernebre.zone_blitz.captcha.google;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.UUID;

public class GoogleReCaptchaVerificationResponseFactory {
    public static GoogleReCaptchaVerificationResponse generateOneDto() {
        return generateOneDto(true);
    }

    public static GoogleReCaptchaVerificationResponse generateOneDto(boolean success) {
        return GoogleReCaptchaVerificationResponse.builder()
                .success(success)
                .challengeTimestamp(ZonedDateTime.now())
                .hostname(UUID.randomUUID().toString())
                .errorCodes(Collections.emptySet())
                .build();
    }
}
