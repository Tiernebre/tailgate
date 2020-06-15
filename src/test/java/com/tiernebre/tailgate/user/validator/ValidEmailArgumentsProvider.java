package com.tiernebre.tailgate.user.validator;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public final class ValidEmailArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("email@example.com"),
                Arguments.of("firstname.lastname@example.com"),
                Arguments.of("email@subdomain.example.com"),
                Arguments.of("firstname+lastname@example.com"),
                Arguments.of("email@123.123.123.123"),
                Arguments.of("email@[123.123.123.123]"),
                Arguments.of("\"email\"@example.com"),
                Arguments.of("1234567890@example.com"),
                Arguments.of("email@example-one.com"),
                Arguments.of("_______@example.com"),
                Arguments.of("email@example.name"),
                Arguments.of("email@example.museum"),
                Arguments.of("email@example.co.jp"),
                Arguments.of("firstname-lastname@example.com")
        );
    }
}
