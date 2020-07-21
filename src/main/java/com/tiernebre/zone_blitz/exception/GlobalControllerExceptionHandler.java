package com.tiernebre.zone_blitz.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalControllerExceptionHandler {
    static final String GENERIC_ERROR_MESSAGE = "An error on the server occurred. Please retry again or double check your information.";

    @ExceptionHandler(InvalidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidException(InvalidException invalidException) {
        return ErrorResponse.builder()
                .errors(invalidException.getErrors())
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleInvalidException() {
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGlobalError(Exception globalError) {
        String errorLog;
        if (globalError instanceof RuntimeException) {
            errorLog = "Encountered RuntimeException: ";
        } else {
            errorLog = "Encountered unhandled exception error: ";
        }
        log.error(errorLog, globalError);
        log.info("Proceeding to respond with generic error message for a globally encountered error.");
        return ErrorResponse.of(GENERIC_ERROR_MESSAGE);
    }
}
