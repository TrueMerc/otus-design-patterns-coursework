package ru.ryabtsev.antifraud.transactions.traits;

/**
 * The basic interface for classes that have identifiers that have been got from an external system.
 * @param <T> type on identifier.
 */
public interface ExternalIdContainer<T> {

    T getExternalId();
}
