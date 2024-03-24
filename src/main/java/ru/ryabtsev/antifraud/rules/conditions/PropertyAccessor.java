package ru.ryabtsev.antifraud.rules.conditions;

import java.util.function.Function;

public class PropertyAccessor<T, U, R> {

    private final T object;

    private final Function<U, R> accessMethod;

    public PropertyAccessor(final T object, final Function<U, R> accessMethod) {
        this.object = object;
        this.accessMethod = accessMethod;
    }

    public R get() {
        return accessMethod.apply((U) object);
    }
}
