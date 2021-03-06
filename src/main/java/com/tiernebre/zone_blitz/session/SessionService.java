package com.tiernebre.zone_blitz.session;

import com.tiernebre.zone_blitz.token.access.GenerateAccessTokenException;

import java.util.UUID;

public interface SessionService {
    /**
     * Creates a session with an access token based upon the given request.
     * @param createSessionRequest The request used to build the access token as a part of the overall session.
     * @return A session with an access token to allow the user to access the application.
     * @throws GenerateAccessTokenException if the access token had an error while being generated.
     * @throws UserNotFoundForSessionException if the user information provided does not exist for a current user.
     * @throws InvalidCreateSessionRequestException if the request to create a session was not valid.
     */
    SessionDto createOne(CreateSessionRequest createSessionRequest) throws InvalidCreateSessionRequestException, UserNotFoundForSessionException, GenerateAccessTokenException;

    /**
     * Refreshes a session by checking the validity of a given refresh token.
     *
     * @param refreshToken The refresh token used to refresh a session.
     * @return A brand new session with a new access token and refresh token if the refresh token was valid.
     * @throws GenerateAccessTokenException if the access token had an error while being generated.
     * @throws InvalidRefreshSessionRequestException if the refresh token is invalid or expired.
     */
    SessionDto refreshOne(UUID refreshToken) throws GenerateAccessTokenException, InvalidRefreshSessionRequestException;
}
