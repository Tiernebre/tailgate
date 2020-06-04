package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.test.EmailIntegrationTestSuite;
import com.tiernebre.tailgate.test.email.TestEmail;
import com.tiernebre.tailgate.test.email.TestEmailInboxService;
import com.tiernebre.tailgate.test.email.TestEmailSearchOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;

public class UserEmailConfirmationServiceIntegrationTests extends EmailIntegrationTestSuite {
    @Autowired
    private UserEmailConfirmationService userEmailConfirmationService;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private TestEmailInboxService testEmailInboxService;

    @Nested
    @DisplayName("sendOne")
    public class SendOneTests {
        @Test
        @DisplayName("sends an email to an SMTP server")
        public void sendsAnEmailToAnSmtpServer() {
            UsersRecord userToConfirm = userRecordPool.createAndSaveOne();
            UserDto userToConfirmAsDto = UserDto.builder()
                    .id(userToConfirm.getId())
                    .email(userToConfirm.getEmail())
                    .build();
            userEmailConfirmationService.sendOne(userToConfirmAsDto);
            TestEmail foundEmail = testEmailInboxService.searchForEmail(
                    TestEmailSearchOption.TO,
                    userToConfirm.getEmail()
            );
            assertNotNull(foundEmail.getTo());
            assertNotNull(foundEmail.getFrom());
        }
    }
}
