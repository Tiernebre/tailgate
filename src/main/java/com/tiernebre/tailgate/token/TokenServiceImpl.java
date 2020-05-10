package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {
    private final static String NON_EXISTENT_USER_ERROR = "The request to create a token included information that did not match up with an existing user.";

    private final TokenProvider provider;
    private final UserService userService;
    private final TokenValidator validator;

    @Override
    public String createOne(CreateTokenRequest createTokenRequest) throws UserNotFoundForTokenException, GenerateTokenException, InvalidCreateTokenRequestException {
        validator.validate(createTokenRequest);
        UserDto foundUser = userService
                .findOneByEmailAndPassword(
                        createTokenRequest.getEmail(),
                        createTokenRequest.getPassword()
                )
                .orElseThrow(
                        () -> new UserNotFoundForTokenException(NON_EXISTENT_USER_ERROR)
                );
        return provider.generateOne(foundUser);
    }
}
