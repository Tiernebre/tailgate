package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.converter.Converter;

/**
 * Provides conversion operations between the User data types.
 */
public interface UserConverter extends Converter<UserDto, UserEntity> {
    /**
     * Converts a create request into an entity.
     *
     * @param createRequest The create request to convert.
     * @return An entity version of the create request.
     */
    UserEntity convertFromCreateRequest(CreateUserRequest createRequest);
}
