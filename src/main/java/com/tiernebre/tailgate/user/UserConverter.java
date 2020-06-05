package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.converter.CreatableConverter;

/**
 * Provides conversion operations between the User data types.
 */
public interface UserConverter extends CreatableConverter<UserDto, UserEntity, CreateUserRequest> {
}
