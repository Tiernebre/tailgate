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
        if (response == null || !response.isSuccess()) {
            throw new CaptchaIsNotValidException();
        }
    }
}
