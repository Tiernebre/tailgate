package com.tiernebre.tailgate.security_questions;

import com.tiernebre.tailgate.converter.ConverterImpl;

import java.util.function.Function;

public class SecurityQuestionConverterImpl extends ConverterImpl<SecurityQuestionDto, SecurityQuestionEntity> implements SecurityQuestionConverter {
    /**
     * Constructor.
     *
     * @param fromDto    Function that converts given dto entity into the domain entity.
     * @param fromEntity Function that converts given domain entity into the dto entity.
     */
    public SecurityQuestionConverterImpl(Function<SecurityQuestionDto, SecurityQuestionEntity> fromDto, Function<SecurityQuestionEntity, SecurityQuestionDto> fromEntity) {
        super(fromDto, fromEntity);
    }
}
