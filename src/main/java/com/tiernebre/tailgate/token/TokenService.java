package com.tiernebre.tailgate.token;

public interface TokenService {
    /**
     * Creates an authentication access token from a given request.
     * @param createAccessTokenRequest The details about the requester of the authentication (typically account information).
     * @return The token representation for an authorized session.
     */
    String createAccessToken(CreateAccessTokenRequest createAccessTokenRequest) throws UserNotFoundForTokenException, GenerateTokenException, InvalidCreateTokenRequestException;
}
