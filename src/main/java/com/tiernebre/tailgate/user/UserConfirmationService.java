package com.tiernebre.tailgate.user;

/**
 * Service that handles confirmations for users.
 */
public interface UserConfirmationService {
    /**
     * Asynchronously sends a confirmation request to a user to ensure that they own their requested user.
     * @param userToConfirm The details of the user  to send a confirmation request to.
     */
    void sendOne(UserDto userToConfirm);
}
