package com.tiernebre.tailgate.token.user_confirmation;

import com.tiernebre.tailgate.user.dto.UserDto;

import java.util.Optional;

public interface UserConfirmationTokenRepository {
    /**
     * Creates a confirmation token for a provided user.
     *
     * @param user The user to create a confirmation token for.
     * @return The created confirmation token.
     */
    UserConfirmationTokenEntity createOneForUser(UserDto user);

    /**
     * Gets a confirmation token for a provided user.
     *
     * @param user The user to find a confirmation token for.
     * @return A filled optional if found, otherwise an empty optional if not found.
     */
    Optional<UserConfirmationTokenEntity> findOneForUser(UserDto user);
}
