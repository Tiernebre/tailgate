package com.tiernebre.tailgate.authentication;

import com.tiernebre.tailgate.token.TokenProvider;
import com.tiernebre.tailgate.user.UserDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String TOKEN_HEADER = "Authorization";

    private final TokenProvider tokenProvider;

    @Autowired
    public JwtAuthorizationFilter(
            AuthenticationManager authenticationManager,
            TokenProvider tokenProvider
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
            UsernamePasswordAuthenticationToken authentication = getAuthentication(tokenProvided);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(req, res);
    }

    private boolean tokenIsProvidedInCorrectFormat(String tokenProvided) {
        return StringUtils.isNotBlank(tokenProvided) && tokenProvided.startsWith(TOKEN_PREFIX);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDTO user = tokenProvider.validateOne(token.replace(TOKEN_PREFIX, ""));
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
    }
}
