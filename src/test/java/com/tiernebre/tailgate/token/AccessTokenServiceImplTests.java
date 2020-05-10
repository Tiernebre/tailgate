package com.tiernebre.tailgate.token;

import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccessTokenServiceImplTests {
    @Mock
    private UserService userService;

    @Mock
    private AccessTokenProvider tokenProvider;

    @Mock
    private TokenValidator tokenValidator;

    @InjectMocks
    private AccessTokenServiceImpl tokenService;

    @Nested
    @DisplayName("createOne")
    public class CreateOneTests {
        @Test
        @DisplayName("returns the generated token if entirely successful")
        void returnsTheGeneratedTokenIfSuccessful() throws GenerateAccessTokenException, UserNotFoundForTokenException, InvalidCreateAccessTokenRequestException {
            UserDto user = UserFactory.generateOneDto();
            String password = UUID.randomUUID().toString();
            CreateAccessTokenRequest createAccessTokenRequest = CreateAccessTokenRequest.builder()
                    .email(user.getEmail())
                    .password(password)
                    .build();
            doNothing().when(tokenValidator).validate(eq(createAccessTokenRequest));
            when(userService.findOneByEmailAndPassword(eq(user.getEmail()), eq(password))).thenReturn(Optional.of(user));
            String expectedToken = UUID.randomUUID().toString();
            when(tokenProvider.generateOne(eq(user))).thenReturn(expectedToken);
            String createdToken = tokenService.createOne(createAccessTokenRequest);
            assertEquals(expectedToken, createdToken);
        }

        @Test
        @DisplayName("throws UserNotFoundForTokenException if the user could not be found from the request")
        void throwsUserNotFoundForTokenExceptionIfUserCouldNotBeFound() throws InvalidCreateAccessTokenRequestException {
            UserDto user = UserFactory.generateOneDto();
            String password = UUID.randomUUID().toString();
            CreateAccessTokenRequest createAccessTokenRequest = CreateAccessTokenRequest.builder()
                    .email(user.getEmail())
                    .password(password)
                    .build();
            doNothing().when(tokenValidator).validate(eq(createAccessTokenRequest));
            when(userService.findOneByEmailAndPassword(eq(user.getEmail()), eq(password))).thenReturn(Optional.empty());
            UserNotFoundForTokenException thrown = assertThrows(UserNotFoundForTokenException.class, () -> tokenService.createOne(createAccessTokenRequest));
            assertEquals("The request to create a token included information that did not match up with an existing user.", thrown.getMessage());
        }
    }
}
