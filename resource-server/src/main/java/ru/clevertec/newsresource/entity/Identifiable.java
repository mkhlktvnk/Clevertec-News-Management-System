package ru.clevertec.newsresource.entity;

import java.io.Serializable;

/**
 * An interface for entities that have an identifying property.
 * @param <T> the type of the identifying property
 */
public interface Identifiable<T extends Serializable> {

    /**
     * Returns the identifying property of the entity.
     * @return the identifying property
     */
    T getId();
}

