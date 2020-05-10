package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.*;
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
public class SessionServiceImplTests {
    @Mock
    private AccessTokenService accessTokenService;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Nested
    @DisplayName("createOne")
    class CreateOneTests {
        @Test
        @DisplayName("returns a properly mapped session DTO representation with the tokens")
        public void createOneReturnsDto() throws InvalidCreateAccessTokenRequestException, UserNotFoundForAccessTokenException, GenerateAccessTokenException {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            String expectedAccessToken = UUID.randomUUID().toString();
            when(accessTokenService.createOne(eq(createSessionRequest))).thenReturn(expectedAccessToken);
            SessionDto expectedSession = SessionDto.builder()
                    .accessToken(expectedAccessToken)
                    .build();
            SessionDto createdSession = sessionService.createOne(createSessionRequest);
            assertEquals(expectedSession, createdSession);
        }
    }
}
