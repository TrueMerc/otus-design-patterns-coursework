package ru.ryabtsev.antifraud.rules.conditions;

import ru.ryabtsev.antifraud.conditional.actions.Condition;

public record InstanceOfClass(Object object, Class<?> possibleClassOfObject) implements Condition {

    @Override
    public boolean isFulfilled() {
        return possibleClassOfObject.isInstance(object);
    }
}
