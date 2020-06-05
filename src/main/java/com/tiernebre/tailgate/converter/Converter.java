package com.tiernebre.tailgate.converter;

import java.util.Collection;
import java.util.List;

/**
 * Generic converter, thanks to Java8 features not only provides a way of generic bidirectional
 * conversion between corresponding types, but also a common way of converting a collection of
 * objects of the same type, reducing boilerplate code to the absolute minimum.
 *
 * @param <T> DTO representation's type
 * @param <U> Domain representation's type
 */
public interface Converter<T, U> {
    /**
     * Converts DTO to Entity.
     *
     * @param dto DTO entity
     * @return The domain representation - the result of the converting function application on dto
     *     entity.
     */
    U convertFromDto(final T dto);

    /**
     * Converts Entity to DTO.
     *
     * @param entity domain entity
     * @return The DTO representation - the result of the converting function application on domain
     *     entity.
     */
    T convertFromEntity(final U entity);

    /**
     * Converts list of DTOs to list of Entities.
     *
     * @param dtos collection of DTO entities
     * @return List of domain representation of provided entities retrieved by mapping each of them
     *     with the conversion function
     */
    List<U> createFromDtos(final Collection<T> dtos);

    /**
     * Converts list of Entities to list of DTOs.
     *
     * @param entities collection of domain entities
     * @return List of domain representation of provided entities retrieved by mapping each of them
     *     with the conversion function
     */
    List<T> createFromEntities(final Collection<U> entities);
}
