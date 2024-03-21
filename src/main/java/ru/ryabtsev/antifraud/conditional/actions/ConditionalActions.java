package ru.ryabtsev.antifraud.conditional.actions;

import java.util.ArrayList;
import java.util.List;
import ru.ryabtsev.antifraud.actions.Action;
import ru.ryabtsev.antifraud.traits.Executable;

public class ConditionalActions<T> implements Executable<T> {

    private final List<ConditionalAction<T>> conditionalActions;

    private final Action<T> defaultAction;

    public ConditionalActions(final List<ConditionalAction<T>> conditionalActions, final Action<T> defaultAction) {
        this.conditionalActions = conditionalActions;
        this.defaultAction = defaultAction;
    }

    @Override
    public T execute() {
        return conditionalActions.stream()
                .filter(ConditionalAction::canBeExecuted)
                .findFirst()
                .map(ConditionalAction::getAction)
                .orElse(defaultAction)
                .execute();
    }
}
