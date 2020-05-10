package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccessTokenServiceImpl implements AccessTokenService {
    private final static String NON_EXISTENT_USER_ERROR = "The request to create an access token included information that did not match up with an existing user.";

    private final AccessTokenProvider provider;
    private final UserService userService;
    private final AccessTokenValidator validator;

    @Override
    public String createOne(CreateAccessTokenRequest createAccessTokenRequest) throws UserNotFoundForAccessTokenException, GenerateAccessTokenException, InvalidCreateAccessTokenRequestException {
        validator.validate(createAccessTokenRequest);
        UserDto foundUser = userService
                .findOneByEmailAndPassword(
                        createAccessTokenRequest.getEmail(),
                        createAccessTokenRequest.getPassword()
                )
                .orElseThrow(
                        () -> new UserNotFoundForAccessTokenException(NON_EXISTENT_USER_ERROR)
                );
        return provider.generateOne(foundUser);
    }
}
