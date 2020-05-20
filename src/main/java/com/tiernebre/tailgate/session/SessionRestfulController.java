package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.GenerateAccessTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionRestfulController {
    static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    private final SessionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionDto createOne(
            @RequestBody CreateSessionRequest createSessionRequest,
            HttpServletResponse httpServletResponse
    ) throws GenerateAccessTokenException, UserNotFoundForSessionException, InvalidCreateSessionRequestException {
        SessionDto createdSession = service.createOne(createSessionRequest);
        setRefreshTokenCookieFromSession(createdSession, httpServletResponse);
        return createdSession;
    }

    public SessionDto refreshOne(String refreshToken, HttpServletResponse httpServletResponse) throws GenerateAccessTokenException, InvalidRefreshSessionRequestException {
        SessionDto refreshedSession = service.refreshOne(refreshToken);
        setRefreshTokenCookieFromSession(refreshedSession, httpServletResponse);
        return refreshedSession;
    }

    private void setRefreshTokenCookieFromSession(SessionDto session, HttpServletResponse httpServletResponse) {
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, session.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        httpServletResponse.addCookie(refreshTokenCookie);
    }
}
