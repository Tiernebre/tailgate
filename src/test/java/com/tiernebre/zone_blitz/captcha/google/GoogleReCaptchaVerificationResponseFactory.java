package com.tiernebre.zone_blitz.captcha.google;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.UUID;

public class GoogleReCaptchaVerificationResponseFactory {
    public static GoogleReCaptchaVerificationResponse generateOneDto() {
        return generateOneDto(true, BigDecimal.ONE);
    }

    public static GoogleReCaptchaVerificationResponse generateOneDto(boolean success, BigDecimal score) {
        return GoogleReCaptchaVerificationResponse.builder()
                .success(success)
                .score(score)
                .action(UUID.randomUUID().toString())
                .challengeTimestamp(ZonedDateTime.now())
                .hostname(UUID.randomUUID().toString())
                .errorCodes(Collections.emptySet())
                .build();
    }
}
