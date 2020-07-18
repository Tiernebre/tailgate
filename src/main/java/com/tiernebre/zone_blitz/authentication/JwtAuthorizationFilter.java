package com.tiernebre.zone_blitz.authentication;

import com.tiernebre.zone_blitz.token.access.AccessTokenInvalidException;
import com.tiernebre.zone_blitz.token.access.AccessTokenProvider;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String TOKEN_HEADER = "Authorization";

    private final AccessTokenProvider tokenProvider;

    @Autowired
    public JwtAuthorizationFilter(
            AuthenticationManager authenticationManager,
            AccessTokenProvider tokenProvider
    ) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws IOException, ServletException {
        String tokenProvided = req.getHeader(TOKEN_HEADER);
        if (tokenIsProvidedInCorrectFormat(tokenProvided)) {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(tokenProvided, getAccessTokenFingerprintFromCookies(req));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, res);
    }

    private boolean tokenIsProvidedInCorrectFormat(String tokenProvided) {
        return StringUtils.isNotBlank(tokenProvided) && tokenProvided.startsWith(TOKEN_PREFIX);
    }

    private String getAccessTokenFingerprintFromCookies(HttpServletRequest req) {
        if (ArrayUtils.isNotEmpty(req.getCookies())) {
            return Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals(SessionCookieNames.FINGERPRINT_COOKIE_NAME))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        } else {
            return null;
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token, String fingerprint) throws AccessTokenInvalidException {
        UserDto user = tokenProvider.validateOne(token.replace(TOKEN_PREFIX, ""), fingerprint);
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }
}
