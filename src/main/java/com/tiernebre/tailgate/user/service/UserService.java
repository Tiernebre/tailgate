package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.user.dto.CreateUserRequest;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.exception.InvalidUserException;
import com.tiernebre.tailgate.user.exception.UserAlreadyExistsException;

import java.util.Optional;

public interface UserService {
    /**
     * Creates a user.
     * @param createUserRequest The specifications of the user to create.
     * @return The created user.
     * @throws InvalidUserException If the request to create a user is invalid.
     */
    UserDto createOne(CreateUserRequest createUserRequest) throws InvalidUserException, UserAlreadyExistsException;

    /**
     * Finds a user by a given email and password.
     * @param email The email of a user to find.
     * @param password The password of a user to find.
     * @return An optional containing the user if found, or an empty optional if no user was found.
     */
    Optional<UserDto> findOneByEmailAndPassword(String email, String password);

    /**
     * Finds a user by a given valid, non-expired refresh token.
     * @param refreshToken The refresh token to find an associated user for..
     * @return An optional containing a user if the refresh token is valid and non-expired, or an empty optional if the
     *         refresh token is expired or invalid.
     */
    Optional<UserDto> findOneByNonExpiredRefreshToken(String refreshToken);
}
