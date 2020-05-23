package com.tiernebre.tailgate.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class StringValidatorTests {
    @Nested
    @DisplayName("requireNonBlank")
    public class RequireNonBlankTests {
        @Test
        @DisplayName("throws StringIsBlankException with provided message if the string to validate is null")
        public void throwsStringIsBlankExceptionWithProvidedMessageIfTheStringToValidateIsNull() {
            String expectedMessage = "Expected Test Failure.";
            StringIsBlankException thrownException = assertThrows(StringIsBlankException.class, () -> StringValidator.requireNonBlank(null, expectedMessage));
            assertEquals(expectedMessage, thrownException.getMessage());
        }

        @ParameterizedTest(name = "throws StringIsBlankException with provided message if the string to validate is equal to \"{0}\"")
        @ValueSource(strings = {
                "",
                " ",
                "  ",
                "    ",
                "                             "
        })
        public void throwsStringIsBlankExceptionWithProvidedMessageIfTheStringToValidateIs(String stringToValidate) {
            String expectedMessage = "Expected Test Failure.";
            StringIsBlankException thrownException = assertThrows(StringIsBlankException.class, () -> StringValidator.requireNonBlank(stringToValidate, expectedMessage));
            assertEquals(expectedMessage, thrownException.getMessage());
        }
    }
}
