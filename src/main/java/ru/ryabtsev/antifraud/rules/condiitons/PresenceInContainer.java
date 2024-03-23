package ru.ryabtsev.antifraud.rules.condiitons;

import java.util.function.Function;
import ru.ryabtsev.antifraud.conditional.actions.Condition;
import ru.ryabtsev.antifraud.traits.Container;

public class PresenceInContainer<T, U> implements Condition {

    private final Object valueHolder;

    private final Function<U, ? extends T> valueExtractor;

    private final Container<T> container;

    public PresenceInContainer(
            final Object valueHolder,
            final Function<U, T> valueExtractor,
            final Container<T> container) {
        this.valueHolder = valueHolder;
        this.valueExtractor = valueExtractor;
        this.container = container;
    }

    @Override
    public boolean isFulfilled() {
        return container.contains(valueExtractor.apply((U) valueHolder));
    }
}
