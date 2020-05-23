package com.tiernebre.tailgate.token.refresh;

import com.tiernebre.tailgate.user.UserDto;
import com.tiernebre.tailgate.user.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.tiernebre.tailgate.token.refresh.RefreshTokenConstants.NULL_USER_ERROR_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTests {
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Nested
    class CreateOneForUserTests {
        @Test
        @DisplayName("returns the created refresh token")
        public void returnsTheCreatedRefreshToken() {
            UserDto userDto = UserFactory.generateOneDto();
            RefreshTokenEntity expectedRefreshToken = RefreshTokenFactory.generateOneEntity();
            when(refreshTokenRepository.createOneForUser(eq(userDto))).thenReturn(expectedRefreshToken);
            String createdToken = refreshTokenService.createOneForUser(userDto);
            assertEquals(expectedRefreshToken.getToken(), createdToken);
        }

        @Test
        @DisplayName("throws NullPointerException with a helpful error message if the user is null")
        public void throwsNullPointerExceptionWithAHelpfulErrorMessageIfTheUserIsNull() {
            NullPointerException thrownException = assertThrows(NullPointerException.class, () -> refreshTokenService.createOneForUser(null));
            assertEquals(NULL_USER_ERROR_MESSAGE, thrownException.getMessage());
        }
    }

    @Nested
    class DeleteOneTests {
        @Test
        @DisplayName("deletes the given token")
        public void returnsTheCreatedRefreshToken() {
            String refreshTokenToDelete = UUID.randomUUID().toString();
            refreshTokenService.deleteOne(refreshTokenToDelete);
            verify(refreshTokenRepository, times(1)).deleteOne(eq(refreshTokenToDelete));
        }
    }
}
