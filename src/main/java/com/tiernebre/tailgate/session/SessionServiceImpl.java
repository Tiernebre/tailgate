package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final AccessTokenService accessTokenService;

    @Override
    public SessionDto createOne(CreateAccessTokenRequest createAccessTokenRequest) throws InvalidCreateAccessTokenRequestException, UserNotFoundForAccessTokenException, GenerateAccessTokenException {
        return SessionDto.builder()
                .accessToken(accessTokenService.createOne(createAccessTokenRequest))
                .build();
    }
}
