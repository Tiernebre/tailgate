package com.tiernebre.tailgate.converter;

import java.util.function.Function;

public abstract class CreatableConverterImpl<T, U, V> extends ConverterImpl<T, U> implements CreatableConverter<T, U, V> {
    private final Function<V, U> fromCreateOrUpdateRequest;

    /**
     * Constructor.
     *
     * @param fromDto                   Function that converts given dto entity into the domain entity.
     * @param fromEntity                Function that converts given domain entity into the dto entity.
     * @param fromCreateOrUpdateRequest Function that converts given create or update request entity into the domain entity.
     */
    public CreatableConverterImpl(
            Function<T, U> fromDto,
            Function<U, T> fromEntity,
            Function<V, U> fromCreateOrUpdateRequest
    ) {
        super(fromDto, fromEntity);
        this.fromCreateOrUpdateRequest = fromCreateOrUpdateRequest;
    }

    @Override
    public final U convertFromCreateOrUpdateRequest(final V createOrUpdateRequest) {
        return fromCreateOrUpdateRequest.apply(createOrUpdateRequest);
    }
}
