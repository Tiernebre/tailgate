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
public class TokenRestfulControllerTests {
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private TokenRestfulController tokenRestfulController;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the created token")
        void returnsTheCreatedToken () throws UserNotFoundForTokenException, GenerateTokenException, InvalidCreateTokenRequestException {
            CreateTokenRequest createTokenRequest = TokenFactory.generateOneCreateRequest();
            String expectedToken = UUID.randomUUID().toString();
            when(tokenService.createOne(eq(createTokenRequest))).thenReturn(expectedToken);
            TokenDTO gottenTokenDTO = tokenRestfulController.createOne(createTokenRequest);
            assertEquals(expectedToken, gottenTokenDTO.getToken());
        }
    }
}
