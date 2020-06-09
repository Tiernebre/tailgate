package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.UserFactory;
import com.tiernebre.tailgate.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AsyncUserEmailConfirmationServiceTests {
    @Mock
    private UserEmailConfirmationService userEmailConfirmationService;

    @InjectMocks
    private AsyncUserEmailConfirmationService asyncUserEmailConfirmationService;

    @Nested
    @DisplayName("sendOne")
    public class SendOneTests {
        @Test
        @DisplayName("asynchronously sends user confirmation")
        public void asynchronouslySendsUserConfirmation() {
            UserDto userDto = UserFactory.generateOneDto();
            asyncUserEmailConfirmationService.sendOne(userDto);
            verify(userEmailConfirmationService, times(1)).sendOne(eq(userDto));
        }
    }
}
