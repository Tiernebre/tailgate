package com.tiernebre.tailgate.token;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Allows for RESTful operations on tokens.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tokens")
public class TokenRestfulController {
    private final TokenService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenDTO createOne(@RequestBody CreateAccessTokenRequest createAccessTokenRequest) throws GenerateTokenException, UserNotFoundForTokenException, InvalidCreateTokenRequestException {
        return AccessTokenDTO.builder()
                .token(service.createAccessToken(createAccessTokenRequest))
                .build();
    }
}
