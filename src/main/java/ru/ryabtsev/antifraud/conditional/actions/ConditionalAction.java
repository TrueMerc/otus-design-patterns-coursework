package ru.ryabtsev.antifraud.conditional.actions;

import ru.ryabtsev.antifraud.actions.Action;
import ru.ryabtsev.antifraud.conditions.Condition;

/**
 * The interface that describes container for a condition and an appropriate action.
 * @param <T>
 */
public interface ConditionalAction<T> extends Action<T> {

    Condition getCondition();

    Action<T> getAction();

    boolean canBeExecuted();
}
