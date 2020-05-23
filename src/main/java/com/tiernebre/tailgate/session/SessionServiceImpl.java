package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.AccessTokenProvider;
import com.tiernebre.tailgate.token.GenerateAccessTokenException;
import com.tiernebre.tailgate.token.refresh.RefreshTokenService;
import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    final static String NON_EXISTENT_USER_FOR_CREATE_ERROR = "The request to create a session included information that did not match up with an existing user.";
    final static String INVALID_REFRESH_TOKEN_ERROR = "The request to refresh a session had either an invalid or expired refresh token.";

    private final AccessTokenProvider accessTokenProvider;
    private final SessionValidator validator;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public SessionDto createOne(CreateSessionRequest createSessionRequest) throws InvalidCreateSessionRequestException, UserNotFoundForSessionException, GenerateAccessTokenException {
        validator.validate(createSessionRequest);
        UserDto userToCreateSessionFor = userService
                .findOneByEmailAndPassword(
                        createSessionRequest.getEmail(),
                        createSessionRequest.getPassword()
                )
                .orElseThrow(
                        () -> new UserNotFoundForSessionException(NON_EXISTENT_USER_FOR_CREATE_ERROR)
                );
        return buildOutSessionForUser(userToCreateSessionFor);
    }

    @Override
    public SessionDto refreshOne(String originalRefreshToken) throws GenerateAccessTokenException, InvalidRefreshSessionRequestException {
        validator.validateRefreshToken(originalRefreshToken);
        UserDto userToCreateRefreshedSessionFor = userService
                .findOneByNonExpiredRefreshToken(originalRefreshToken)
                .orElseThrow(() -> new InvalidRefreshSessionRequestException(INVALID_REFRESH_TOKEN_ERROR));
        SessionDto refreshedSession = buildOutSessionForUser(userToCreateRefreshedSessionFor);
        refreshTokenService.deleteOne(originalRefreshToken);
        return refreshedSession;
    }

    private SessionDto buildOutSessionForUser(UserDto user) throws GenerateAccessTokenException {
        return SessionDto.builder()
                .accessToken(accessTokenProvider.generateOne(user))
                .refreshToken(refreshTokenService.createOneForUser(user))
                .build();
    }
}
