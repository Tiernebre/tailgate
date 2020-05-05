package com.tiernebre.tailgate.user;

import com.tiernebre.tailgate.converter.ConverterImpl;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl extends ConverterImpl<UserDTO, UserEntity> implements UserConverter {
    public UserConverterImpl() {
        super(UserConverterImpl::convertToEntity, UserConverterImpl::convertToDto);
    }

    public UserEntity convertFromCreateRequest(CreateUserRequest createRequest) {
        return UserEntity.builder()
                .id(null)
                .email(createRequest.getEmail())
                .password(createRequest.getPassword())
                .build();
    }

    private static UserDTO convertToDto(UserEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .build();
    }

    private static UserEntity convertToEntity(UserDTO dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .build();
    }
}
