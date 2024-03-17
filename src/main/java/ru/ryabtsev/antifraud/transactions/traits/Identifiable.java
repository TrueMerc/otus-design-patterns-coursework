package ru.ryabtsev.antifraud.transactions.traits;

/**
 * The interface for all classes that can be identified.
 * @param <T> the type of identifier.
 */
public interface Identifiable<T> {

    T getId();
}
