package com.tiernebre.tailgate.token;

public interface TokenService {
    /**
     * Creates an authentication token from a given request.
     * @param createTokenRequest The details about the requester of the authentication (typically account information).
     * @return The token representation for an authorized session.
     */
    String createOne(CreateTokenRequest createTokenRequest) throws UserNotFoundForTokenException, GenerateTokenException, InvalidCreateTokenRequestException;
}
