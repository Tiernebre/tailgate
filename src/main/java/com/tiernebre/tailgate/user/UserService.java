package com.tiernebre.tailgate.user;

import java.util.Optional;

public interface UserService {
    /**
     * Creates a user.
     * @param createUserRequest The specifications of the user to create.
     * @return The created user.
     * @throws InvalidUserException If the request to create a user is invalid.
     */
    UserDTO createOne(CreateUserRequest createUserRequest) throws InvalidUserException, UserAlreadyExistsException;

    /**
     * Finds a user by a given email and password.
     * @param email The email of a user to find.
     * @param password The password of a user to find.
     * @return An optional containing the user if found, or an empty optional if no user was found.
     */
    Optional<UserDTO> findOneByEmailAndPassword(String email, String password);
}
