package com.tiernebre.zone_blitz.user.service;

import com.tiernebre.zone_blitz.converter.Converter;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.entity.UserEntity;

/**
 * Provides conversion operations between the User data types.
 */
public interface UserConverter extends Converter<UserDto, UserEntity> {
}
