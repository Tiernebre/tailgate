package com.tiernebre.tailgate.security_questions;

import com.tiernebre.tailgate.converter.ConverterImpl;

public class SecurityQuestionConverterImpl extends ConverterImpl<SecurityQuestionDto, SecurityQuestionEntity> implements SecurityQuestionConverter {
    public SecurityQuestionConverterImpl() {
        super(
                SecurityQuestionConverterImpl::convertToEntity,
                SecurityQuestionConverterImpl::convertToDto
        );
    }

    private static SecurityQuestionDto convertToDto(SecurityQuestionEntity entity) {
        return null;
    }

    private static SecurityQuestionEntity convertToEntity(SecurityQuestionDto dto) {
        return SecurityQuestionEntity.builder()
                .id(dto.getId())
                .question(dto.getQuestion())
                .build();
    }
}
