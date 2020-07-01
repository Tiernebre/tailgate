package com.tiernebre.zone_blitz.captcha.google;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoogleReCaptchaVerifierTests {
    @InjectMocks
    private GoogleReCaptchaVerifier googleReCaptchaVerifier;

    @Mock
    private RestTemplate googleReCaptchaRestTemplate;

    @Mock
    private GoogleReCaptchaConfigurationProperties googleReCaptchaConfigurationProperties;

    @Nested
    @DisplayName("verify")
    class VerifyTests {
        @Test
        @DisplayName("does not throw any errors if the captcha token is valid")
        void doesNothingIfTheCaptchaTokenIsValid() {
            String secret = UUID.randomUUID().toString();
            when(googleReCaptchaConfigurationProperties.getSecret()).thenReturn(secret);
            String captchaToken = UUID.randomUUID().toString();
            GoogleReCaptchaVerificationResponse mockedGoogleResponse = GoogleReCaptchaVerificationResponseFactory.generateOneDto();
            when(googleReCaptchaRestTemplate.postForObject(
                    eq("/siteverify?secret={secret}&response={response}"),
                    isNull(),
                    eq(GoogleReCaptchaVerificationResponse.class),
                    eq(secret),
                    eq(captchaToken)
            )).thenReturn(mockedGoogleResponse);
            assertDoesNotThrow(() -> googleReCaptchaVerifier.verify(captchaToken));
        }
    }
}
