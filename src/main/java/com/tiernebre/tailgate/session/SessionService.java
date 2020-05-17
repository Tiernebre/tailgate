package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.GenerateAccessTokenException;

public interface SessionService {
    /**
     * Creates a session with an access token based upon the given request.
     * @param createSessionRequest The request used to build the access token as a part of the overall session.
     * @return A session with an access token to allow the user to access the application.
     */
    SessionDto createOne(CreateSessionRequest createSessionRequest) throws InvalidCreateSessionRequestException, UserNotFoundForSessionException, GenerateAccessTokenException;

    /**
     * Refreshes a session by checking the validity of a given refresh token.
     *
     * @param refreshToken The refresh token used to refresh a session.
     * @return A brand new session with a new access token and refresh token if the refresh token was valid.
     */
    SessionDto refreshOne(String refreshToken) throws GenerateAccessTokenException, InvalidRefreshSessionRequestException;
}
