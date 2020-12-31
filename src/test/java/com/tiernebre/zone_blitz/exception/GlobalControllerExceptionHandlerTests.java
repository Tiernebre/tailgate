package com.tiernebre.zone_blitz.exception;

import com.tiernebre.zone_blitz.session.InvalidCreateSessionRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingRequestCookieException;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static com.tiernebre.zone_blitz.exception.GlobalControllerExceptionHandler.GENERIC_ERROR_MESSAGE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
        void returnsAnErrorResponseContainingTheInvalidExceptionErrorsForARuntimeException() throws Exception {
            RuntimeException runtimeException = new RuntimeException("Generic Runtime Exception!");
            ErrorResponse errorResponse = globalControllerExceptionHandler.handleGlobalError(runtimeException);
            assertEquals(1, errorResponse.getErrors().size());
            assertTrue(errorResponse.getErrors().contains(GENERIC_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("returns an error response that contains a generic error to avoid error leakage for an exception")
        void returnsAnErrorResponseContainingTheInvalidExceptionErrorsForAnException() throws Exception {
            Exception exception = new Exception("Generic Exception!");
            ErrorResponse errorResponse = globalControllerExceptionHandler.handleGlobalError(exception);
            assertEquals(1, errorResponse.getErrors().size());
            assertTrue(errorResponse.getErrors().contains(GENERIC_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("re-throws the exception if the exception is an AccessDeniedException")
        void rethrowsTheExceptionIfTheExceptionIsAnAccessDeniedException() {
            AccessDeniedException originalException = new AccessDeniedException(UUID.randomUUID().toString());
            AccessDeniedException thrownException = assertThrows(
                    AccessDeniedException.class,
                    () -> globalControllerExceptionHandler.handleGlobalError(originalException)
            );
            assertEquals(originalException, thrownException);
        }

        @Test
        @DisplayName("re-throws the exception if the exception is a MissingRequestCookieException")
        void rethrowsTheExceptionIfTheExceptionIsAMissingRequestCookieException() {
            MissingRequestCookieException originalException = new MissingRequestCookieException(UUID.randomUUID().toString(), mock(MethodParameter.class));
            MissingRequestCookieException thrownException = assertThrows(
                    MissingRequestCookieException.class,
                    () -> globalControllerExceptionHandler.handleGlobalError(originalException)
            );
            assertEquals(originalException, thrownException);
        }
    }
}
