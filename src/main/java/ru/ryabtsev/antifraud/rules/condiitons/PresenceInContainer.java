package ru.ryabtsev.antifraud.rules.condiitons;

import ru.ryabtsev.antifraud.conditional.actions.Condition;
import ru.ryabtsev.antifraud.traits.Container;

public class PresenceInContainer<T> implements Condition {

    private final T value;

    private final Container<T> container;

    public PresenceInContainer(final T value, final Container<T> container) {
        this.value = value;
        this.container = container;
    }

    @Override
    public boolean isFulfilled() {
        return container.contains(value);
    }
}
