package com.tiernebre.tailgate.test.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class MailhogApi {
    private final RestTemplate mailhogRestTemplate;

    public MailhogSearchResponse search(String kind, String query) {
        return mailhogRestTemplate.getForObject(
                "/search?kind={kind}&query={query}",
                MailhogSearchResponse.class,
                kind,
                query
        );
    }
}
