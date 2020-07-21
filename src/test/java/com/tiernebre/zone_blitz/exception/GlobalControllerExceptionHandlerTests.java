package com.tiernebre.zone_blitz.exception;

import com.tiernebre.zone_blitz.session.InvalidCreateSessionRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GlobalControllerExceptionHandlerTests {
    private static final GlobalControllerExceptionHandler globalControllerExceptionHandler = new GlobalControllerExceptionHandler();

    @Nested
    @DisplayName("handleInvalidException")
    class HandleInvalidExceptionTests {
        @Test
        @DisplayName("returns an error response containing the invalid exception errors")
        void returnsAnErrorResponseContainingTheInvalidExceptionErrors() {
            Set<String> errorMessages = Collections.singleton("Expected Test Error Message");
            InvalidCreateSessionRequestException invalidCreateSessionRequestException = new InvalidCreateSessionRequestException(errorMessages);
            ErrorResponse errorResponse = globalControllerExceptionHandler.handleInvalidException(invalidCreateSessionRequestException);
            assertEquals(errorMessages, errorResponse.getErrors());
        }
    }
}
