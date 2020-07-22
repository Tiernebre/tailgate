package com.tiernebre.zone_blitz.exception;

import java.nio.file.AccessDeniedException;
import java.util.List;

public class GlobalControllerExceptionHandlerConfiguration {
    public static final List<Class<?>> EXCEPTIONS_TO_LET_SPRING_HANDLE = List.of(
            AccessDeniedException.class
    );
}
