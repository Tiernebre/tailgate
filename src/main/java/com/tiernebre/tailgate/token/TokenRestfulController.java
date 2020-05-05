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
    public TokenDTO createOne(@RequestBody CreateTokenRequest createTokenRequest) throws GenerateTokenException, UserNotFoundForTokenException, InvalidCreateTokenRequestException {
        return TokenDTO.builder()
                .token(service.createOne(createTokenRequest))
                .build();
    }
}
