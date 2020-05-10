package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.converter.ConverterImpl;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl extends ConverterImpl<UserDto, UserEntity, CreateUserRequest> implements UserConverter {
    public UserConverterImpl() {
        super(
                UserConverterImpl::convertToEntity,
                UserConverterImpl::convertToDto,
                UserConverterImpl::convertFromCreateRequest
        );
    }

    private static UserEntity convertFromCreateRequest(CreateUserRequest createRequest) {
        return UserEntity.builder()
                .id(null)
                .email(createRequest.getEmail())
                .password(createRequest.getPassword())
                .build();
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
