package ru.ryabtsev.antifraud.conditional.actions;

public class ConditionalActionWithAlternative<T> implements ConditionalAction<T> {

    private final Condition condition;

    private final Action<T> action;

    private final Action<T> alternativeAction;

    private final ExecutionCondition executionCondition;

    public ConditionalActionWithAlternative(
            final Condition condition,
            final Action<T> action,
            final Action<T> alternativeAction,
            final ExecutionCondition executionCondition) {
        this.condition = condition;
        this.action = action;
        this.alternativeAction = alternativeAction;
        this.executionCondition = executionCondition;
    }

    public ConditionalActionWithAlternative(
            final Condition condition,
            final Action<T> action,
            final Action<T> alternativeAction) {
        this(condition, action, alternativeAction, ExecutionCondition.FULFILLMENT);
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

    @Override
    public Action<T> getAction() {
        return canBeExecutedInCaseOfFulfillment() || canBeExecutedInCaseOfViolation()
                ? action
                : alternativeAction;
    }

    private boolean canBeExecutedInCaseOfFulfillment() {
        return condition.isFulfilled() && executionCondition.isExecutableWhen(true);
    }

    private boolean canBeExecutedInCaseOfViolation() {
        return condition.isViolated() && executionCondition.isExecutableWhen(false);
    }

    @Override
    public boolean canBeExecuted() {
        return true;
    }

    @Override
    public T execute() {
        return getAction().execute();
    }
}
