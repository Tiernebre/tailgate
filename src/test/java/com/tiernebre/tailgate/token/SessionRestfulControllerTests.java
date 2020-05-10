package com.tiernebre.tailgate.token;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionRestfulControllerTests {
    @Mock
    private AccessTokenService accessTokenService;

    @InjectMocks
    private SessionRestfulController sessionRestfulController;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the created token")
        void returnsTheCreatedToken () throws UserNotFoundForTokenException, GenerateTokenException, InvalidCreateTokenRequestException {
            CreateAccessTokenRequest createAccessTokenRequest = TokenFactory.generateOneCreateRequest();
            String expectedToken = UUID.randomUUID().toString();
            when(accessTokenService.createOne(eq(createAccessTokenRequest))).thenReturn(expectedToken);
            SessionDto gottenSessionDto = sessionRestfulController.createOne(createAccessTokenRequest);
            assertEquals(expectedToken, gottenSessionDto.getAccessToken());
        }
    }
}
