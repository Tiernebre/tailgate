package com.tiernebre.zone_blitz.user.validator;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class InvalidEmailArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of("plainaddress"),
                Arguments.of("#@%^%#$@#$@#.com"),
                Arguments.of("@example.com"),
                Arguments.of("Joe Smith <email@example.com>"),
                Arguments.of("email.example.com"),
                Arguments.of("email@example@example.com"),
                Arguments.of(".email@example.com"),
                Arguments.of("email.@example.com"),
                Arguments.of("email..email@example.com"),
                Arguments.of("email@example.com (Joe Smith)"),
                Arguments.of("email@-example.com"),
                Arguments.of("email@example..com"),
                Arguments.of("Abc..123@example.com")
        );
    }
}
