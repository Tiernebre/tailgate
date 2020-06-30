package com.tiernebre.zone_blitz.converter;

import java.util.function.Function;

public class StubConverter extends ConverterImpl<StubDto, StubEntity> {
    /**
     * Constructor.
     *
     * @param fromDto    Function that converts given dto entity into the domain entity.
     * @param fromEntity Function that converts given domain entity into the dto entity.
     */
    public StubConverter(Function<StubDto, StubEntity> fromDto, Function<StubEntity, StubDto> fromEntity) {
        super(fromDto, fromEntity);
    }
}
