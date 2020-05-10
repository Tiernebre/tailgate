package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.*;
import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.tiernebre.tailgate.session.SessionServiceImpl.NON_EXISTENT_USER_ERROR;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionServiceImplTests {
    @Mock
    private AccessTokenProvider accessTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private SessionValidator sessionValidator;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Nested
    @DisplayName("createOne")
    class CreateOneTests {
        @Test
        @DisplayName("returns a properly mapped session DTO representation with the tokens")
        public void createOneReturnsDto() throws InvalidCreateAccessTokenRequestException, UserNotFoundForAccessTokenException, GenerateAccessTokenException {
            UserDto user = UserFactory.generateOneDto();
            String password = UUID.randomUUID().toString();
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .email(user.getEmail())
                    .password(password)
                    .build();
            when(userService.findOneByEmailAndPassword(eq(user.getEmail()), eq(password))).thenReturn(Optional.of(user));
            doNothing().when(sessionValidator).validate(createSessionRequest);
            String expectedToken = UUID.randomUUID().toString();
            SessionDto expectedSession = SessionDto.builder().accessToken(expectedToken).build();
            when(accessTokenProvider.generateOne(eq(user))).thenReturn(expectedToken);
            SessionDto createdSession = sessionService.createOne(createSessionRequest);
            Assertions.assertEquals(expectedSession, createdSession);
        }

        @Test
        @DisplayName("throws UserNotFoundForAccessTokenException if the user could not be found from the request")
        public void createOneThrowsNotFoundException() throws InvalidCreateAccessTokenRequestException {
            UserDto user = UserFactory.generateOneDto();
            String password = UUID.randomUUID().toString();
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .email(user.getEmail())
                    .password(password)
                    .build();
            when(userService.findOneByEmailAndPassword(eq(user.getEmail()), eq(password))).thenReturn(Optional.empty());
            doNothing().when(sessionValidator).validate(createSessionRequest);
            UserNotFoundForAccessTokenException thrown = assertThrows(UserNotFoundForAccessTokenException.class, () -> sessionService.createOne(createSessionRequest));
            Assertions.assertEquals(NON_EXISTENT_USER_ERROR, thrown.getMessage());
        }
    }
}
