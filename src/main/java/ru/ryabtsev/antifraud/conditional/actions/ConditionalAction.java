package ru.ryabtsev.antifraud.conditional.actions;

/**
 * The interface that describes container for a condition and an appropriate action.
 * @param <T>
 */
public interface ConditionalAction<T> extends Action<T> {

    /**
     * Returns condition.
     * @return condition.
     */
    Condition getCondition();

    /**
     * Returns action that can be executed.
     * @return action that can be executed.
     */
    Action<T> getAction();

    /**
     * Returns {@code true} if action can be executed.
     * @return {@code true} if action can be executed.
     */
    boolean canBeExecuted();
}
