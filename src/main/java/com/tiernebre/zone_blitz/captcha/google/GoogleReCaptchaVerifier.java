package com.tiernebre.zone_blitz.captcha.google;

import com.tiernebre.zone_blitz.captcha.CaptchaIsNotValidException;
import com.tiernebre.zone_blitz.captcha.CaptchaVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleReCaptchaVerifier implements CaptchaVerifier {
    private final RestTemplate googleReCaptchaRestTemplate;
    private final GoogleReCaptchaConfigurationProperties configurationProperties;

    @Override
    public void verify(String captchaToken) throws CaptchaIsNotValidException {
        String secret = configurationProperties.getSecret();
        GoogleReCaptchaVerificationResponse response = googleReCaptchaRestTemplate.postForObject(
                "/siteverify?secret={secret}&response={response}",
                null,
                GoogleReCaptchaVerificationResponse.class,
                secret,
                captchaToken
        );
        checkForInvalidTokenResponse(response);
        ensureValidTokenMeetsCriteria(response);
    }

    private void checkForInvalidTokenResponse(GoogleReCaptchaVerificationResponse response) throws CaptchaIsNotValidException {
        if (response == null || !response.isSuccess()) {
            throw new CaptchaIsNotValidException();
        }
    }

    private void ensureValidTokenMeetsCriteria(GoogleReCaptchaVerificationResponse response) throws CaptchaIsNotValidException {
        if (response.getScore().compareTo(configurationProperties.getMinimumAllowedScore()) < 0) {
            throw new CaptchaIsNotValidException();
        }
    }
}
