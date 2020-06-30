package com.tiernebre.zone_blitz.validator;

import org.apache.commons.lang3.StringUtils;

/**
 * Static class for validating Strings.
 */
public class StringValidator {
    public static void requireNonBlank(String stringToValidate, String message) {
        if (StringUtils.isBlank(stringToValidate)) {
            throw new StringIsBlankException(message);
        }
    }
}
