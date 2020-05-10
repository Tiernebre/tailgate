package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.GenerateAccessTokenException;

public interface SessionService {
    /**
     * Creates a session with an access token based upon the given request.
     * @param createSessionRequest The request used to build the access token as a part of the overall session.
     * @return A session with an access token to allow the user to access the application.
     */
    SessionDto createOne(CreateSessionRequest createSessionRequest) throws InvalidCreateSessionRequestException, UserNotFoundForSessionException, GenerateAccessTokenException;
}
