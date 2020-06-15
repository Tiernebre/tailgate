package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.user.dto.ResetTokenUpdatePasswordRequest;
import com.tiernebre.tailgate.user.exception.InvalidUpdatePasswordRequestException;
import com.tiernebre.tailgate.validator.BaseValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserPasswordValidatorImpl extends BaseValidator implements UserPasswordValidator {
    private static final String PASSWORD_MATCHES_ERROR = "password and confirmationPassword must equal each other";
    private static final String PASSWORD_CONTAIN_DIGITS_ERROR = "password must contain numerical digits (0-9)";
    private static final String PASSWORD_MIXED_CHARACTERS_ERROR = "password must contain mixed uppercase and lowercase alphabetical characters (A-Z, a-z)";
    private static final String PASSWORD_SPECIAL_CHARACTERS_ERROR = "password must contain non-digit and non-alphanumeric characters";

    private static final String DIGITS_REGEX = ".*\\d.*";
    private static final String SPECIAL_CHARACTERS_REGEX = "[^a-z0-9 ]";

    @Autowired
    public UserPasswordValidatorImpl(Validator validator) {
        super(validator);
    }

    @Override
    public void validateUpdateRequest(ResetTokenUpdatePasswordRequest updatePasswordRequest) throws InvalidUpdatePasswordRequestException {
        Set<String> errors = validateCommon(updatePasswordRequest);
        if (CollectionUtils.isNotEmpty(errors)) {
            throw new InvalidUpdatePasswordRequestException(errors);
        }
    }

    @Override
    public Set<String> validate(String password, String confirmationPassword) {
        Set<String> errorsFound = new HashSet<>();
        validatePassword(errorsFound, password, confirmationPassword);
        return errorsFound;
    }

    private void validatePassword(Set<String> errorsFound, String password, String confirmationPassword) {
        if (StringUtils.isNotBlank(password)) {
            validatePasswordHasMixedLowercaseAndUpperCaseCharacters(errorsFound, password);
            validatePasswordHasNumericalDigitCharacters(errorsFound, password);
            validatePasswordHasSpecialCharacters(errorsFound, password);
            validatePasswordMatchesConfirmationPassword(errorsFound, password, confirmationPassword);
        }
    }

    private void validatePasswordHasNumericalDigitCharacters(Set<String> errorsFound, String password) {
        if (!password.matches(DIGITS_REGEX)) {
            errorsFound.add(PASSWORD_CONTAIN_DIGITS_ERROR);
        }
    }

    private void validatePasswordHasMixedLowercaseAndUpperCaseCharacters(Set<String> errorsFound, String password) {
        if (!StringUtils.isMixedCase(password)) {
            errorsFound.add(PASSWORD_MIXED_CHARACTERS_ERROR);
        }
    }

    private void validatePasswordHasSpecialCharacters(Set<String> errorsFound, String password) {
        Pattern specialCharactersPattern = Pattern.compile(SPECIAL_CHARACTERS_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher specialCharactersMatcher = specialCharactersPattern.matcher(password);
        if (!specialCharactersMatcher.find()) {
            errorsFound.add(PASSWORD_SPECIAL_CHARACTERS_ERROR);
        }
    }

    private void validatePasswordMatchesConfirmationPassword(Set<String> errorsFound, String password, String confirmationPassword) {
        if (!StringUtils.equals(password, confirmationPassword)) {
            errorsFound.add(PASSWORD_MATCHES_ERROR);
        }
    }
}
