package ru.ryabtsev.antifraud.conditional.actions;

public class DefaultConditionalAction<T> implements ConditionalAction<T> {

    private final Condition condition;

    private final Action<T> action;

    private final ExecutionCondition executionCondition;
    
    private boolean canBeExecuted;

    private boolean isConditionCalculated;

    public DefaultConditionalAction(
            final Condition condition, final Action<T> action, final ExecutionCondition executionCondition) {
        this.condition = condition;
        this.action = action;
        this.executionCondition = executionCondition;
        canBeExecuted = false;
        isConditionCalculated = false;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

    @Override
    public Action<T> getAction() {
        return action;
    }

    @Override
    public boolean canBeExecuted() {
        if (!isConditionCalculated) {
            canBeExecuted = canBeExecutedInCaseOfFulfillment() || canBeExecutedInCaseOfViolation();
            isConditionCalculated = true;
        }
        return canBeExecuted;
    }

    private boolean canBeExecutedInCaseOfFulfillment() {
        return condition.isFulfilled() && executionCondition.isExecutableWhen(true);
    }

    private boolean canBeExecutedInCaseOfViolation() {
        return condition.isViolated() && executionCondition.isExecutableWhen(false);
    }

    @Override
    public T execute() {
        if (!isConditionCalculated) {
            canBeExecuted();
        }
        if (canBeExecuted) {
            return action.execute();
        } else {
            throw new IllegalStateException(
                    "Can't execute conditional the action " + this + " because the condition isn't appropriate");
        }
    }
}
