package ru.ryabtsev.antifraud.caches;

import ru.ryabtsev.antifraud.traits.Container;

/**
 * The interface for cache that contains black list of TIN values.
 */
public interface BlackTinCache extends Container<String> {

    boolean contains(final String tin);
}
