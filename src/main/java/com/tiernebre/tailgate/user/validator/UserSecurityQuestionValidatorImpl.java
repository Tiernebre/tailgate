package com.tiernebre.tailgate.user.validator;

import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserSecurityQuestionValidatorImpl implements UserSecurityQuestionValidator {
    @Override
    public Set<String> validate(CreateUserRequest createUserRequest) {
        return null;
    }
}
