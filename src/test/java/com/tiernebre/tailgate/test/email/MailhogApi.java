package com.tiernebre.tailgate.test.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class MailhogApi {
    private final RestTemplate mailhogRestTemplate;

    public MailhogApiResponse search(String kind, String query, int start, int limit) {
        return mailhogRestTemplate.getForObject(
                "/search?kind={kind}&query={query}&start={start}&limit={limit}",
                MailhogApiResponse.class,
                kind,
                query,
                start,
                limit
        );
    }
}
