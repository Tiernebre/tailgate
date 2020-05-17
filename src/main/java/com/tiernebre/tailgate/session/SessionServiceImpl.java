package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.AccessTokenProvider;
import com.tiernebre.tailgate.token.GenerateAccessTokenException;
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
        return SessionDto.builder()
                .accessToken(accessTokenProvider.generateOne(userToCreateSessionFor))
                .build();
    }

    @Override
    public SessionDto refreshOne(String refreshToken) throws GenerateAccessTokenException, InvalidRefreshSessionRequestException {
        validator.validateRefreshToken(refreshToken);
        UserDto userToCreateRefreshedSessionFor = userService
                .findOneByNonExpiredRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshSessionRequestException(INVALID_REFRESH_TOKEN_ERROR));
        return SessionDto.builder()
                .accessToken(accessTokenProvider.generateOne(userToCreateRefreshedSessionFor))
                .build();
    }
}
