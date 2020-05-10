package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccessTokenServiceImpl implements AccessTokenService {
    private final static String NON_EXISTENT_USER_ERROR = "The request to create a token included information that did not match up with an existing user.";

    private final AccessTokenProvider provider;
    private final UserService userService;
    private final TokenValidator validator;

    @Override
    public String createOne(CreateAccessTokenRequest createAccessTokenRequest) throws UserNotFoundForTokenException, GenerateTokenException, InvalidCreateTokenRequestException {
        validator.validate(createAccessTokenRequest);
        UserDto foundUser = userService
                .findOneByEmailAndPassword(
                        createAccessTokenRequest.getEmail(),
                        createAccessTokenRequest.getPassword()
                )
                .orElseThrow(
                        () -> new UserNotFoundForTokenException(NON_EXISTENT_USER_ERROR)
                );
        return provider.generateOne(foundUser);
    }
}
