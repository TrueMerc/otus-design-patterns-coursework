package ru.ryabtsev.antifraud.conditional.actions;

public enum ExecutionCondition {
    FULFILLMENT(true),
    VIOLATION(false);

    private final boolean executionCondition;

    ExecutionCondition(final boolean executionCondition) {
        this.executionCondition = executionCondition;
    }

    public boolean isExecutableWhen(final boolean condition) {
        return executionCondition == condition;
    }
}
