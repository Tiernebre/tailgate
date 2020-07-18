package com.tiernebre.zone_blitz.session;

import com.tiernebre.zone_blitz.token.access.AccessTokenProvider;
import com.tiernebre.zone_blitz.token.access.GenerateAccessTokenException;
import com.tiernebre.zone_blitz.token.access.fingerprint.AccessTokenFingerprintGenerator;
import com.tiernebre.zone_blitz.token.refresh.RefreshTokenService;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    final static String NON_EXISTENT_USER_FOR_CREATE_ERROR = "The request to create a session included information that did not match up with an existing user.";
    final static String INVALID_REFRESH_TOKEN_ERROR = "The request to refresh a session had either an invalid or expired refresh token.";

    private final AccessTokenProvider accessTokenProvider;
    private final SessionValidator validator;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenFingerprintGenerator accessTokenFingerprintGenerator;

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
    public SessionDto refreshOne(UUID originalRefreshToken) throws GenerateAccessTokenException, InvalidRefreshSessionRequestException {
        UserDto userToCreateRefreshedSessionFor = userService
                .findOneByNonExpiredRefreshToken(originalRefreshToken)
                .orElseThrow(() -> new InvalidRefreshSessionRequestException(INVALID_REFRESH_TOKEN_ERROR));
        SessionDto refreshedSession = buildOutSessionForUser(userToCreateRefreshedSessionFor);
        refreshTokenService.deleteOne(originalRefreshToken);
        return refreshedSession;
    }

    private SessionDto buildOutSessionForUser(UserDto user) throws GenerateAccessTokenException {
        String fingerprint = accessTokenFingerprintGenerator.generateOne();
        return SessionDto.builder()
                .accessToken(accessTokenProvider.generateOne(user, fingerprint))
                .refreshToken(refreshTokenService.createOneForUser(user))
                .fingerprint(fingerprint)
                .build();
    }
}
