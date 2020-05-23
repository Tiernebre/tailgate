package com.tiernebre.tailgate.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class StringValidatorTests {
    @Nested
    @DisplayName("requireNonBlank")
    public class RequireNonBlankTests {
        @ParameterizedTest(name = "throws StringIsBlankException with provided message if the string to validate is equal to \"{0}\"")
        @ValueSource(
                strings = {
                        "",
                        " ",
                        "  ",
                        "    ",
                        "                             "
                }
        )
        @NullSource
        public void throwsStringIsBlankExceptionWithProvidedMessageIfTheStringToValidateIs(String stringToValidate) {
            String expectedMessage = "Expected Test Failure.";
            StringIsBlankException thrownException = assertThrows(StringIsBlankException.class, () -> StringValidator.requireNonBlank(stringToValidate, expectedMessage));
            assertEquals(expectedMessage, thrownException.getMessage());
        }

        @ParameterizedTest(name = "does not throw StringIsBlankException with provided message if the string to validate is equal to \"{0}\"")
        @ValueSource(strings = {
                "a",
                "1",
                "a1",
                "Hello World!",
                "Testing 1, 2, 3...",
                "null",
                "undefined"
        })
        public void doesNotThrowStringIsBlankExceptionWithProvidedMessageIfTheStringToValidateIs(String stringToValidate) {
            assertDoesNotThrow(() -> StringValidator.requireNonBlank(stringToValidate, ""));
        }
    }
}
