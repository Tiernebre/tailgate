package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String REQUIRED_CREATE_USER_REQUEST_MESSAGE = "The create user request is a required parameter and must not be null";
    private static final String REQUIRED_EMAIL_MESSAGE = "The email to find a user for is a required parameter and must not be null";
    private static final String REQUIRED_PASSWORD_MESSAGE = "The password to find a user for is a required parameter and must not be null";

    private final UserRepository repository;
    private final UserConverter converter;
    private final UserValidator validator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createOne(CreateUserRequest createUserRequest) throws InvalidUserException, UserAlreadyExistsException {
        Objects.requireNonNull(createUserRequest, REQUIRED_CREATE_USER_REQUEST_MESSAGE);

        validator.validate(createUserRequest);
        validateThatEmailDoesNotExist(createUserRequest.getEmail());
        String encryptedPassword = passwordEncoder.encode(createUserRequest.getPassword());
        CreateUserRequest encryptedCreateUserRequest = createUserRequest.withPassword(encryptedPassword);
        UserEntity entityToCreate = converter.convertFromCreateOrUpdateRequest(encryptedCreateUserRequest);
        UserEntity entityCreated = repository.saveOne(entityToCreate);
        return converter.convertFromEntity(entityCreated);
    }

    @Override
    public Optional<UserDto> findOneByEmailAndPassword(String email, String password) {
        Objects.requireNonNull(email, REQUIRED_EMAIL_MESSAGE);
        Objects.requireNonNull(password, REQUIRED_PASSWORD_MESSAGE);

        Optional<UserEntity> foundUserByEmail = repository.findOneByEmail(email);
        return foundUserByEmail
                .filter(foundUser -> passwordEncoder.matches(password, foundUser.getPassword()))
                .map(converter::convertFromEntity);
    }

    @Override
    public Optional<UserDto> findOneByNonExpiredRefreshToken(String refreshToken) {
        return Optional.empty();
    }

    private void validateThatEmailDoesNotExist(String email) throws UserAlreadyExistsException {
        if (repository.oneExistsByEmail(email)) {
            throw new UserAlreadyExistsException();
        }
    }
}
