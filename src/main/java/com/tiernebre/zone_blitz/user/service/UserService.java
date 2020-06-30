package com.tiernebre.zone_blitz.user.service;

import com.tiernebre.zone_blitz.user.dto.CreateUserRequest;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.exception.InvalidUserException;
import com.tiernebre.zone_blitz.user.exception.UserAlreadyExistsException;
import com.tiernebre.zone_blitz.user.exception.UserNotFoundForConfirmationException;

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
     * Finds a user by a given email.
     * @param email The email of a user to find.
     * @return An optional containing the user if found, or an empty optional if no user was found.
     */
    Optional<UserDto> findOneByEmail(String email);

    /**
     * Finds a user by a given valid, non-expired refresh token.
     * @param refreshToken The refresh token to find an associated user for..
     * @return An optional containing a user if the refresh token is valid and non-expired, or an empty optional if the
     *         refresh token is expired or invalid.
     */
    Optional<UserDto> findOneByNonExpiredRefreshToken(String refreshToken);

    /**
     * Confirms a user given a confirmation token.
     * @param confirmationToken The token to confirm a user.
     */
    void confirmOne(String confirmationToken) throws UserNotFoundForConfirmationException;
}
