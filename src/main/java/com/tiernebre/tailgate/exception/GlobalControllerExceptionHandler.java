package com.tiernebre.tailgate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(InvalidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidException(InvalidException invalidException) {
        return ErrorResponse.builder()
                .errors(invalidException.getErrors())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleAnyException(Exception e) {
        log.error("Caught unhandled exception: ", e);
    }
}
