package com.tiernebre.zone_blitz.user.repository;

import java.util.Optional;

/**
 * Manages passwords for a user.
 */
public interface UserPasswordRepository {
    /**
     * Updates a user's password using a given reset token and a user email.
     * @param password The password to update to.
     * @param email The email of the user to update
     * @param resetToken The password reset token also tied to a password reset request.
     * @return true if the user was found and updated, false if no user was found and updated with the criteria provided.
     */
    boolean updateOneWithEmailAndNonExpiredResetToken(String password, String email, String resetToken);

    /**
     * Updates a user's password using a given user id.
     * @param id the id of the user to update.
     * @return true if the user was found and updated, false if no user was found and updated with the criteria provided.
     */
    boolean updateOneForId(Long id, String password);

    /**
     * Gets the hashed password for a given user ID.
     * @param id the id of the user to find a password of.
     * @return an optional containing the found password, or an empty optional if the id was not valid..
     */
    Optional<String> findOneForId(Long id);
}
