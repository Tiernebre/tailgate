package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.repository.Repository;

import java.util.Optional;

/**
 * Provides operations to perform on Users data.
 */
public interface UserRepository extends Repository<UserEntity, Long> {
    /**
     * Returns a UserEntity with a specific email.
     * @param email The email to find a user based off of.
     * @return An optional contiaining a user if one was found,
     *         or an empty optional if no users with the provided email exist.
     */
    Optional<UserEntity> findOneByEmail(String email);

    /**
     * Sees if a user with a provided email exists.
     * @param email The email to find a user based off of.
     * @return true if a user with the email exists, false if there is not.
     */
    boolean oneExistsByEmail(String email);
}
