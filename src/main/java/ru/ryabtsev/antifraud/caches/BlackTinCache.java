package ru.ryabtsev.antifraud.caches;

/**
 * The interface for cache that contains black list of TIN values.
 */
public interface BlackTinCache {

    boolean contains(final String tin);
}
