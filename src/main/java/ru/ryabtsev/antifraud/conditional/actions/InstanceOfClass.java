package ru.ryabtsev.antifraud.conditional.actions;

public record InstanceOfClass(Object object, Class<?> possibleClassOfObject) implements Condition {

    @Override
    public boolean isFulfilled() {
        return possibleClassOfObject.isInstance(object);
    }
}
