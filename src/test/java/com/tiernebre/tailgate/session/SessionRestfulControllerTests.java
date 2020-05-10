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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionRestfulControllerTests {
    @Mock
    private SessionService sessionService;

    @InjectMocks
    private SessionRestfulController sessionRestfulController;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the created token")
        void returnsTheCreatedToken () throws UserNotFoundForAccessTokenException, GenerateAccessTokenException, InvalidCreateAccessTokenRequestException {
            CreateSessionRequest createSessionRequest = TokenFactory.generateOneCreateRequest();
            String expectedToken = UUID.randomUUID().toString();
            SessionDto expectedSession = SessionDto.builder().accessToken(expectedToken).build();
            when(sessionService.createOne(eq(createSessionRequest))).thenReturn(expectedSession);
            SessionDto gottenSession = sessionRestfulController.createOne(createSessionRequest);
            assertEquals(expectedSession, gottenSession);
        }
    }
}
