package ru.ryabtsev.antifraud.conditional.actions;

/**
 * The root interface in condition hierarchy.
 */
public interface Condition {

    /**
     * Returns {@code true} if condition is fulfilled.
     * @return {@code true} if condition is fulfilled.
     */
    boolean isFulfilled();

    /**
     * Returns {@code true} if condition is violated.
     * @return {@code true} if condition is violated.
     */
    default boolean isViolated() {
        return !isFulfilled();
    }
}
