package com.tiernebre.zone_blitz.captcha.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.Set;

@Value
@Builder
public class GoogleReCaptchaVerificationResponse {
    boolean success;
    @JsonProperty("challenge_ts")
    ZonedDateTime challengeTimestamp;
    String hostname;
    @JsonProperty("error-codes")
    Set<String> errorCodes;
}
