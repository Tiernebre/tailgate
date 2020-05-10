package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.session.CreateSessionRequest;

public interface AccessTokenService {
    /**
     * Creates an authentication access token from a given request.
     * @param createSessionRequest The details about the requester of the authentication (typically account information).
     * @return The token representation for an authorized session.
     */
    String createOneForUser(CreateSessionRequest createSessionRequest) throws UserNotFoundForAccessTokenException, GenerateAccessTokenException, InvalidCreateAccessTokenRequestException;
}
