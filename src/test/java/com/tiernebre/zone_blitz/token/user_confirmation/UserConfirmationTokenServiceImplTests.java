package com.tiernebre.zone_blitz.token.user_confirmation;

import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
            when(userConfirmationTokenRepository.createOneForUser(eq(user))).thenReturn(
                    UserConfirmationTokenEntity.builder()
                    .token(expectedConfirmationToken)
                    .build()
            );
            String gottenConfirmationToken = userConfirmationTokenService.createOneForUser(user);
            assertEquals(expectedConfirmationToken, gottenConfirmationToken);
        }
    }

    @Nested
    @DisplayName("findOrGenerateForUser")
    public class FindOrGenerateForUserTests {
        @Test
        @DisplayName("returns the found confirmation token if it exists")
        public void returnsTheCreatedUserConfirmationToken() {
            UserDto user = UserFactory.generateOneDto();
            String expectedConfirmationToken = UUID.randomUUID().toString();
            UserConfirmationTokenEntity userConfirmationTokenEntity = UserConfirmationTokenEntity.builder()
                            .token(expectedConfirmationToken)
                            .build();
            when(userConfirmationTokenRepository.findOneForUser(eq(user))).thenReturn(Optional.of(userConfirmationTokenEntity));
            String tokenGotten = userConfirmationTokenService.findOrGenerateForUser(user);
            assertEquals(expectedConfirmationToken, tokenGotten);
        }

        @Test
        @DisplayName("returns the generated confirmation token if a previous one did not exist")
        public void returnsTheGeneratedConfirmationTokenIfAPreviousOneDidNotExist() {
            UserDto user = UserFactory.generateOneDto();
            String expectedConfirmationToken = UUID.randomUUID().toString();
            UserConfirmationTokenEntity userConfirmationTokenEntity = UserConfirmationTokenEntity.builder()
                    .token(expectedConfirmationToken)
                    .build();
            when(userConfirmationTokenRepository.findOneForUser(eq(user))).thenReturn(Optional.empty());
            when(userConfirmationTokenRepository.createOneForUser(eq(user))).thenReturn(userConfirmationTokenEntity);
            String tokenGotten = userConfirmationTokenService.findOrGenerateForUser(user);
            assertEquals(expectedConfirmationToken, tokenGotten);
        }
    }
}
