package com.tiernebre.tailgate.user.service;

import com.tiernebre.tailgate.converter.ConverterImpl;
import com.tiernebre.tailgate.user.dto.UserDto;
import com.tiernebre.tailgate.user.entity.UserEntity;
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
                .build();
    }

    private static UserEntity convertToEntity(UserDto dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .build();
    }
}
