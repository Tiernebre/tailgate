package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.converter.Converter;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.entity.UserEntity;

/**
 * Provides conversion operations between the User data types.
 */
public interface UserConverter extends Converter<UserDto, UserEntity> {
}
