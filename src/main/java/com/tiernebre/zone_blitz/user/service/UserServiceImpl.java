package com.tiernebre.zone_blitz.user.service;

import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.dto.CreateUserSecurityQuestionRequest;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.entity.UserEntity;
import com.tiernebre.zone_blitz.user.exception.InvalidUserException;
import com.tiernebre.zone_blitz.user.exception.UserAlreadyExistsException;
import com.tiernebre.zone_blitz.user.exception.UserNotFoundForConfirmationException;
import com.tiernebre.zone_blitz.user.repository.UserRepository;
import com.tiernebre.zone_blitz.user.validator.UserValidator;
import com.tiernebre.zone_blitz.validator.StringValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    static final String REQUIRED_EMAIL_MESSAGE = "The email to find a user for is a required parameter and must not be null or blank.";
    static final String REQUIRED_PASSWORD_MESSAGE = "The password to find a user for is a required parameter and must not be null or blank.";

    private final UserRepository repository;
    private final UserConverter converter;
    private final UserValidator validator;
    private final PasswordEncoder passwordEncoder;
    private final UserConfirmationService confirmationService;

    @Override
    public UserDto createOne(CreateUserRequest createUserRequest) throws InvalidUserException, UserAlreadyExistsException {
        validator.validate(createUserRequest);
        validateThatEmailDoesNotExist(createUserRequest.getEmail());
        UserEntity entityCreated = repository.createOne(hashCreateUserRequest(createUserRequest));
        UserDto createdUser = converter.convertFromEntity(entityCreated);
        confirmationService.sendOne(createdUser);
        return createdUser;
    }

    @Override
    public Optional<UserDto> findOneByEmailAndPassword(String email, String password) {
        StringValidator.requireNonBlank(email, REQUIRED_EMAIL_MESSAGE);
        StringValidator.requireNonBlank(password, REQUIRED_PASSWORD_MESSAGE);

        Optional<UserEntity> foundUserByEmail = repository.findOneByEmail(email);
        return foundUserByEmail
                .filter(foundUser -> passwordEncoder.matches(password, foundUser.getPassword()))
                .map(converter::convertFromEntity);
    }

    @Override
    public Optional<UserDto> findOneByEmail(String email) {
        return repository.findOneByEmail(email)
                .map(converter::convertFromEntity);
    }

    @Override
    public Optional<UserDto> findOneByNonExpiredRefreshToken(UUID refreshToken) {
        return repository
                .findOneWithNonExpiredRefreshToken(refreshToken)
                .map(converter::convertFromEntity);
    }

    @Override
    public void confirmOne(UUID confirmationToken) throws UserNotFoundForConfirmationException {
        boolean userWasConfirmed = repository.confirmOne(confirmationToken);
        if (!userWasConfirmed) {
            throw new UserNotFoundForConfirmationException();
        }
    }

    private void validateThatEmailDoesNotExist(String email) throws UserAlreadyExistsException {
        if (repository.oneExistsByEmail(email)) {
            throw new UserAlreadyExistsException();
        }
    }

    private CreateUserRequest hashCreateUserRequest(CreateUserRequest createUserRequest) {
        String hashedPassword = passwordEncoder.encode(createUserRequest.getPassword());
        List<CreateUserSecurityQuestionRequest> hashedSecurityQuestion = hashSecurityQuestion(createUserRequest.getSecurityQuestions());
        return createUserRequest
                .withPassword(hashedPassword)
                .withSecurityQuestions(hashedSecurityQuestion);
    }

    private List<CreateUserSecurityQuestionRequest> hashSecurityQuestion(Collection<CreateUserSecurityQuestionRequest> securityQuestionsToHash) {
        return securityQuestionsToHash.stream()
                .map(this::hashSecurityQuestion)
                .collect(Collectors.toList());
    }

    private CreateUserSecurityQuestionRequest hashSecurityQuestion(CreateUserSecurityQuestionRequest securityQuestionToHash) {
        String hashedAnswer = passwordEncoder.encode(securityQuestionToHash.getAnswer().toLowerCase());
        return securityQuestionToHash.withAnswer(hashedAnswer);
    }
}
