package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.repository.Repository;

import java.util.Optional;

/**
 * Provides operations to perform on Users data.
 */
public interface UserRepository extends Repository<UserEntity, Long, CreateUserRequest> {
    /**
     * Returns a UserEntity with a specific email.
     * @param email The email to find a user based off of.
     * @return An optional containing a user if one was found,
     *         or an empty optional if no users with the provided email exist.
     */
    Optional<UserEntity> findOneByEmail(String email);

    /**
     * Sees if a user with a provided email exists.
     * @param email The email to find a user based off of.
     * @return true if a user with the email exists, false if there is not.
     */
    boolean oneExistsByEmail(String email);

    /**
     * Returns a UserEntity that is tied to a given refresh token.
     *
     * If the refresh token is expired, a user will not be returned as
     * it is considered an invalid token.
     *
     * @param refreshToken The refresh token to find a user for.
     * @return An optional containing the user tied to a given refresh token if the refresh token
     *         is not expired and valid, or an empty optional if the refresh token is expired or invalid.
     */
    Optional<UserEntity> findOneWithNonExpiredRefreshToken(String refreshToken);
}
