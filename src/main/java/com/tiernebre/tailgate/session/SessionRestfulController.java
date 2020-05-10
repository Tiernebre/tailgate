package com.tiernebre.tailgate.session;

import com.tiernebre.tailgate.token.*;
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
    private final SessionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SessionDto createOne(@RequestBody CreateSessionRequest createSessionRequest) throws GenerateAccessTokenException, UserNotFoundForSessionException, InvalidCreateSessionRequestException {
        return service.createOne(createSessionRequest);
    }
}
