package com.tiernebre.tailgate.test.email.mailhog;

import com.tiernebre.tailgate.test.email.TestEmail;
import com.tiernebre.tailgate.test.email.TestEmailInboxService;
import com.tiernebre.tailgate.test.email.TestEmailSearchOption;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Primary
public class MailhogTestEmailInboxService implements TestEmailInboxService {
    private final static String EMPTY_INBOX_ERROR = "The search for emails resulted in an empty list. The email may have possibly failed to get sent to the MailHog inbox.";

    private final RestTemplate mailhogRestTemplate;

    @Override
    public TestEmail searchForEmail(TestEmailSearchOption option, String query) {
        MailhogSearchResponse searchResponse = mailhogRestTemplate.getForObject(
                "/search?kind={kind}&query={query}",
                MailhogSearchResponse.class,
                option.name().toLowerCase(),
                query
        );
        Assert.isTrue(searchResponse.getCount() != 0, EMPTY_INBOX_ERROR);
        return mapMailhogSearchResponseToEmail(searchResponse);
    }

    private TestEmail mapMailhogSearchResponseToEmail(MailhogSearchResponse response) {
        Assert.isTrue(!response.getItems().isEmpty(), EMPTY_INBOX_ERROR);
        MailhogItem firstFoundEmail = response.getItems().get(0);
        String to = firstFoundEmail.getRaw().getTo().stream().findFirst().orElse(null);
        String subject = firstFoundEmail.getContent().getHeaders().getSubject().stream().findFirst().orElse(null);
        return TestEmail.builder()
                .to(to)
                .from(firstFoundEmail.getRaw().getFrom())
                .text(firstFoundEmail.getContent().getBody())
                .subject(subject)
                .build();
    }
}
