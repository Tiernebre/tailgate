package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.exception.InvalidUserException;
import com.tiernebre.tailgate.user.exception.UserAlreadyExistsException;
import com.tiernebre.tailgate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestfulController {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody CreateUserRequest createRequest) throws InvalidUserException, UserAlreadyExistsException {
        return service.createOne(createRequest);
    }
}
