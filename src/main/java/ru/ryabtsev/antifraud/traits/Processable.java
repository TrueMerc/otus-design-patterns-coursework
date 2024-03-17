package ru.ryabtsev.antifraud.traits;

/**
 * This interface makes a class processable.
 */
public interface Processable {

    /**
     * Returns <code>true</code> if transaction is already processed.
     * @return <code>true</code> if transaction is already processed.
     */
    boolean isProcessed();
}
