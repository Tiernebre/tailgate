package com.tiernebre.zone_blitz.user.repository;

import com.tiernebre.zone_blitz.token.password_reset.PasswordResetTokenConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DatePart;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.tiernebre.zone_blitz.jooq.tables.PasswordResetTokens.PASSWORD_RESET_TOKENS;
import static org.jooq.impl.DSL.localDateTimeAdd;

@Component
@RequiredArgsConstructor
public class UserPasswordResetRepositoryUtilities {
    private final PasswordResetTokenConfigurationProperties configurationProperties;

    public Condition passwordResetTokenIsNotExpired() {
        return localDateTimeAdd(
                PASSWORD_RESET_TOKENS.CREATED_AT,
                configurationProperties.getExpirationWindowInMinutes(),
                DatePart.MINUTE
        ).greaterThan(LocalDateTime.now());
    }
}
