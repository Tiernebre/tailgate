package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final AccessTokenService accessTokenService;
    private final SessionValidator validator;

    @Override
    public SessionDto createOne(CreateSessionRequest createSessionRequest) throws InvalidCreateAccessTokenRequestException, UserNotFoundForAccessTokenException, GenerateAccessTokenException {
        validator.validate(createSessionRequest);
        return SessionDto.builder()
                .accessToken(accessTokenService.createOneForUser(createSessionRequest))
                .build();
    }
}
