package com.tiernebre.zone_blitz.exception;


import org.springframework.security.access.AccessDeniedException;

import java.util.Set;

public class GlobalControllerExceptionHandlerConfiguration {
    public static final Set<Class<?>> EXCEPTIONS_TO_LET_SPRING_HANDLE = Set.of(
            AccessDeniedException.class
    );
}
