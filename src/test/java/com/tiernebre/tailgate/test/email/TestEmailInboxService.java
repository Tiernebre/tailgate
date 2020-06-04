package com.tiernebre.tailgate.test.email;

/**
 * Actions to perform look-ups and clean-up actions on a test email inbox.
 */
public interface TestEmailInboxService {
    /**
     * Searches the test email inbox for a specific email.
     *
     * If multiple emails are found with similar traits, the first one found
     * will be returned.
     *
     * @param option The option to use for the provided query.
     * @param query A specific phrase of text to find.
     * @return The email found.
     */
    TestEmail searchForEmail(TestEmailSearchOption option, String query);
}
