package com.tiernebre.tailgate.converter;

/**
 * Converter with options for handling creation conversions.
 *
 * @param <T> DTO representation's type
 * @param <U> Domain representation's type
 * @param <V> Create / Update request for Domain
 */
public interface CreatableConverter<T, U, V> extends Converter<T, U> {
    /**
     * Converts a Create or Update Request to an Entity.
     *
     * @param createOrUpdateRequest the request to create or update a domain
     * @return The domain representation - the result of the converting function application on create
     *     or update request entity.
     */
    U convertFromCreateOrUpdateRequest(final V createOrUpdateRequest);
}
