package com.tiernebre.zone_blitz.user.service;

import com.tiernebre.zone_blitz.converter.ConverterImpl;
import com.tiernebre.zone_blitz.user.dto.UserDto;
import com.tiernebre.zone_blitz.user.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl extends ConverterImpl<UserDto, UserEntity> implements UserConverter {
    public UserConverterImpl() {
        super(
                UserConverterImpl::convertToEntity,
                UserConverterImpl::convertToDto
        );
    }

    private static UserDto convertToDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .isConfirmed(entity.isConfirmed())
                .build();
    }

    private static UserEntity convertToEntity(UserDto dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .build();
    }
}
