package com.tiernebre.zone_blitz.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalControllerExceptionHandler {
    static final String GENERIC_ERROR_MESSAGE = "An error on the server occurred. Please retry again or double check your information.";

    @ExceptionHandler(InvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidException(InvalidException invalidException) {
        return ErrorResponse.builder()
                .errors(invalidException.getErrors())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleInvalidException() {
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleAccessDeniedException() {
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGlobalError(Exception globalError) {
        String errorLog = globalError instanceof RuntimeException ? "Encountered RuntimeException" : "Encountered unhandled exception error";
        log.error(errorLog + ": ", globalError);
        log.info("Proceeding to respond with generic error message for a globally encountered error.");
        return ErrorResponse.of(GENERIC_ERROR_MESSAGE);
    }
}
