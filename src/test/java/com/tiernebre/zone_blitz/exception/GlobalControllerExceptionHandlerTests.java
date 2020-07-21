package com.tiernebre.zone_blitz.exception;

import com.tiernebre.zone_blitz.session.InvalidCreateSessionRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static com.tiernebre.zone_blitz.exception.GlobalControllerExceptionHandler.GENERIC_ERROR_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    @Nested
    @DisplayName("handleGlobalErrorTests")
    class HandleGlobalErrorTests {
        @Test
        @DisplayName("returns an error response that contains a generic error to avoid error leakage for a runtime exception")
        void returnsAnErrorResponseContainingTheInvalidExceptionErrorsForARuntimeException() {
            RuntimeException runtimeException = new RuntimeException("Generic Runtime Exception!");
            ErrorResponse errorResponse = globalControllerExceptionHandler.handleGlobalError(runtimeException);
            assertEquals(1, errorResponse.getErrors().size());
            assertTrue(errorResponse.getErrors().contains(GENERIC_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("returns an error response that contains a generic error to avoid error leakage for an exception")
        void returnsAnErrorResponseContainingTheInvalidExceptionErrorsForAnException() {
            Exception exception = new Exception("Generic Exception!");
            ErrorResponse errorResponse = globalControllerExceptionHandler.handleGlobalError(exception);
            assertEquals(1, errorResponse.getErrors().size());
            assertTrue(errorResponse.getErrors().contains(GENERIC_ERROR_MESSAGE));
        }
    }
}
