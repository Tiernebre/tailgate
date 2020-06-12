package com.tiernebre.tailgate.password;

import com.tiernebre.tailgate.jooq.tables.records.UsersRecord;
import com.tiernebre.tailgate.mail.TailgateEmailConfigurationProperties;
import com.tiernebre.tailgate.test.EmailIntegrationTestSuite;
import com.tiernebre.tailgate.test.email.TestEmail;
import com.tiernebre.tailgate.test.email.TestEmailInboxService;
import com.tiernebre.tailgate.test.email.TestEmailSearchOption;
import com.tiernebre.tailgate.user.UserRecordPool;
import com.tiernebre.tailgate.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PasswordResetTokenEmailDeliveryServiceIntegrationTests extends EmailIntegrationTestSuite {
    @Autowired
    private PasswordResetTokenEmailDeliveryService passwordResetTokenEmailDeliveryService;

    @Autowired
    private UserRecordPool userRecordPool;

    @Autowired
    private TestEmailInboxService testEmailInboxService;

    @Autowired
    private TailgateEmailConfigurationProperties tailgateEmailConfigurationProperties;

    @Autowired
    private PasswordResetEmailDeliveryConfigurationProperties passwordResetEmailDeliveryConfigurationProperties;

    @Nested
    @DisplayName("sendOne")
    public class SendOneTests {
        @Test
        @DisplayName("sends an email to an inbox")
        public void sendsAnEmailToAnInbox() {
            UsersRecord userToResetPasswordFor = userRecordPool.createAndSaveOne();
            UserDto userToResetPasswordForAsDto = UserDto.builder()
                    .id(userToResetPasswordFor.getId())
                    .email(userToResetPasswordFor.getEmail())
                    .build();
            String passwordResetToken = UUID.randomUUID().toString();
            passwordResetTokenEmailDeliveryService.sendOne(userToResetPasswordForAsDto, passwordResetToken);
            TestEmail foundEmail = testEmailInboxService.searchForEmail(
                    TestEmailSearchOption.TO,
                    userToResetPasswordFor.getEmail()
            );
            assertTrue(StringUtils.isNotBlank(foundEmail.getFrom()));
            assertTrue(StringUtils.isNotBlank(foundEmail.getTo()));
            assertTrue(StringUtils.isNotBlank(foundEmail.getSubject()));
            assertTrue(StringUtils.isNotBlank(foundEmail.getText()));
            assertEquals(tailgateEmailConfigurationProperties.getFrom(), foundEmail.getFrom());
            assertEquals(userToResetPasswordFor.getEmail(), foundEmail.getTo());
            assertEquals(passwordResetEmailDeliveryConfigurationProperties.getSubject(), foundEmail.getSubject());
        }
    }
}
