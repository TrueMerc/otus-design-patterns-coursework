package ru.ryabtsev.antifraud.traits;

/**
 * The interface for classes that are containers of entities.
 */
public interface Container<T> {

    boolean contains(final T value);
}
