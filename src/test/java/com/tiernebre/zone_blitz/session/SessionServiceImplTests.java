package com.tiernebre.zone_blitz.session;

import com.tiernebre.zone_blitz.token.access.AccessTokenProvider;
import com.tiernebre.zone_blitz.token.access.GenerateAccessTokenException;
import com.tiernebre.zone_blitz.token.refresh.RefreshTokenService;
import com.tiernebre.zone_blitz.user.UserFactory;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.tiernebre.zone_blitz.session.SessionServiceImpl.INVALID_REFRESH_TOKEN_ERROR;
import static com.tiernebre.zone_blitz.session.SessionServiceImpl.NON_EXISTENT_USER_FOR_CREATE_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceImplTests {
    @Mock
    private AccessTokenProvider accessTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private SessionValidator sessionValidator;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Nested
    @DisplayName("createOne")
    class CreateOneTests {
        @Test
        @DisplayName("returns a properly mapped session DTO representation with the tokens")
        public void createOneReturnsDto() throws InvalidCreateSessionRequestException, UserNotFoundForSessionException, GenerateAccessTokenException {
            UserDto user = UserFactory.generateOneDto();
            String password = UUID.randomUUID().toString();
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .email(user.getEmail())
                    .password(password)
                    .build();
            when(userService.findOneByEmailAndPassword(eq(user.getEmail()), eq(password))).thenReturn(Optional.of(user));
            doNothing().when(sessionValidator).validate(createSessionRequest);
            String expectedAccessToken = UUID.randomUUID().toString();
            UUID expectedRefreshToken = UUID.randomUUID();
            when(refreshTokenService.createOneForUser(eq(user))).thenReturn(expectedRefreshToken);
            SessionDto expectedSession = SessionDto.builder()
                    .accessToken(expectedAccessToken)
                    .refreshToken(expectedRefreshToken)
                    .build();
            when(accessTokenProvider.generateOne(eq(user), anyString())).thenReturn(expectedAccessToken);
            SessionDto createdSession = sessionService.createOne(createSessionRequest);
            assertEquals(expectedSession, createdSession);
        }

        @Test
        @DisplayName("throws not found error if the user could not be found from the request")
        public void createOneThrowsNotFoundException() throws InvalidCreateSessionRequestException {
            UserDto user = UserFactory.generateOneDto();
            String password = UUID.randomUUID().toString();
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .email(user.getEmail())
                    .password(password)
                    .build();
            when(userService.findOneByEmailAndPassword(eq(user.getEmail()), eq(password))).thenReturn(Optional.empty());
            doNothing().when(sessionValidator).validate(createSessionRequest);
            UserNotFoundForSessionException thrown = assertThrows(UserNotFoundForSessionException.class, () -> sessionService.createOne(createSessionRequest));
            assertEquals(NON_EXISTENT_USER_FOR_CREATE_ERROR, thrown.getMessage());
        }

        @Test
        @DisplayName("throws a validation error if the validator found an error")
        public void createOneThrowsValidationException() throws InvalidCreateSessionRequestException {
            UserDto user = UserFactory.generateOneDto();
            String password = UUID.randomUUID().toString();
            CreateSessionRequest createSessionRequest = CreateSessionRequest.builder()
                    .email(user.getEmail())
                    .password(password)
                    .build();
            InvalidCreateSessionRequestException expectedException = new InvalidCreateSessionRequestException(Collections.emptySet());
            doThrow(expectedException).when(sessionValidator).validate(createSessionRequest);
            assertThrows(expectedException.getClass(), () -> sessionService.createOne(createSessionRequest));
        }
    }

    @Nested
    @DisplayName("refreshOne")
    class RefreshOneTests {
        @Test
        @DisplayName("returns a properly mapped session DTO representation with the tokens")
        public void returnsAProperlyMappedSessionDtoRepresentationWithTheTokens() throws GenerateAccessTokenException, InvalidRefreshSessionRequestException {
            UserDto user = UserFactory.generateOneDto();
            UUID refreshToken = UUID.randomUUID();
            when(userService.findOneByNonExpiredRefreshToken(eq(refreshToken))).thenReturn(Optional.of(user));
            String expectedToken = UUID.randomUUID().toString();
            UUID expectedRefreshToken = UUID.randomUUID();
            when(refreshTokenService.createOneForUser(eq(user))).thenReturn(expectedRefreshToken);
            SessionDto expectedSession = SessionDto.builder()
                    .accessToken(expectedToken)
                    .refreshToken(expectedRefreshToken)
                    .build();
            when(accessTokenProvider.generateOne(eq(user), anyString())).thenReturn(expectedToken);
            SessionDto createdSession = sessionService.refreshOne(refreshToken);
            assertEquals(expectedSession, createdSession);
        }

        @Test
        @DisplayName("throws an invalid refresh request exception if the user could not be found from a given refresh token")
        public void throwsAnInvalidRefreshRequestExceptionIfTheUserCouldNotBeFoundFromAGivenRefreshToken() {
            UUID refreshToken = UUID.randomUUID();
            when(userService.findOneByNonExpiredRefreshToken(eq(refreshToken))).thenReturn(Optional.empty());
            InvalidRefreshSessionRequestException thrownException = assertThrows(InvalidRefreshSessionRequestException.class, () -> sessionService.refreshOne(refreshToken));
            assertEquals(INVALID_REFRESH_TOKEN_ERROR, thrownException.getMessage());
        }

        @Test
        @DisplayName("deletes the previously used refresh token")
        public void deletesThePreviouslyUsedRefreshToken() throws GenerateAccessTokenException, InvalidRefreshSessionRequestException {
            UserDto user = UserFactory.generateOneDto();
            UUID originalRefreshToken = UUID.randomUUID();
            when(userService.findOneByNonExpiredRefreshToken(eq(originalRefreshToken))).thenReturn(Optional.of(user));
            when(refreshTokenService.createOneForUser(eq(user))).thenReturn(UUID.randomUUID());
            when(accessTokenProvider.generateOne(eq(user), anyString())).thenReturn(UUID.randomUUID().toString());
            sessionService.refreshOne(originalRefreshToken);
            verify(refreshTokenService, times(1)).deleteOne(eq(originalRefreshToken));
        }
    }
}
