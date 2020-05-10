package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.*;
import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    final static String NON_EXISTENT_USER_ERROR = "The request to create a session included information that did not match up with an existing user.";

    private final AccessTokenProvider accessTokenProvider;
    private final SessionValidator validator;
    private final UserService userService;

    @Override
    public SessionDto createOne(CreateSessionRequest createSessionRequest) throws InvalidCreateAccessTokenRequestException, UserNotFoundForAccessTokenException, GenerateAccessTokenException {
        validator.validate(createSessionRequest);
        UserDto userToCreateSessionFor = userService
                .findOneByEmailAndPassword(
                        createSessionRequest.getEmail(),
                        createSessionRequest.getPassword()
                )
                .orElseThrow(
                        () -> new UserNotFoundForAccessTokenException(NON_EXISTENT_USER_ERROR)
                );
        return SessionDto.builder()
                .accessToken(accessTokenProvider.generateOne(userToCreateSessionFor))
                .build();
    }
}
