package com.tiernebre.tailgate.token.user_confirmation;

import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserConfirmationTokenServiceImplTests {
    @InjectMocks
    private UserConfirmationTokenServiceImpl userConfirmationTokenService;

    @Mock
    private UserConfirmationTokenRepository userConfirmationTokenRepository;

    @Nested
    @DisplayName("createOneForUser")
    public class CreateOneForUserTests {
        @Test
        @DisplayName("returns the created user confirmation token")
        public void returnsTheCreatedUserConfirmationToken() {
            UserDto user = UserFactory.generateOneDto();
            String expectedConfirmationToken = UUID.randomUUID().toString();
            when(userConfirmationTokenRepository.createOneForUser(eq(user))).thenReturn(UserConfirmationTokenEntity.builder()
                    .token(expectedConfirmationToken)
                    .build()
            );
            String gottenConfirmationToken = userConfirmationTokenService.createOneForUser(user);
            assertEquals(expectedConfirmationToken, gottenConfirmationToken);
        }
    }
}
