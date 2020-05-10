package com.tiernebre.tailgate.token;

public interface AccessTokenService {
    /**
     * Creates an authentication access token from a given request.
     * @param createSessionRequest The details about the requester of the authentication (typically account information).
     * @return The token representation for an authorized session.
     */
    String createOne(CreateSessionRequest createSessionRequest) throws UserNotFoundForAccessTokenException, GenerateAccessTokenException, InvalidCreateAccessTokenRequestException;
}
