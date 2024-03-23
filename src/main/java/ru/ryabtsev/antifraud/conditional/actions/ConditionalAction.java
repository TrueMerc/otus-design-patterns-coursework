package ru.ryabtsev.antifraud.conditional.actions;

/**
 * The interface that describes container for a condition and an appropriate action.
 * @param <T>
 */
public interface ConditionalAction<T> extends Action<T> {

    Condition getCondition();

    Action<T> getAction();

    boolean canBeExecuted();
}
