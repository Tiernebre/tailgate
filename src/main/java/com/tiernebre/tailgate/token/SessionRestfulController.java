package com.tiernebre.tailgate.token;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Allows for RESTful operations on tokens.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionRestfulController {
    private final AccessTokenService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenDTO createOne(@RequestBody CreateAccessTokenRequest createAccessTokenRequest) throws GenerateTokenException, UserNotFoundForTokenException, InvalidCreateTokenRequestException {
        return AccessTokenDTO.builder()
                .token(service.createOne(createAccessTokenRequest))
                .build();
    }
}
