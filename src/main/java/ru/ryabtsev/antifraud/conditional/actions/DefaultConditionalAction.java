package ru.ryabtsev.antifraud.conditional.actions;

import ru.ryabtsev.antifraud.actions.Action;
import ru.ryabtsev.antifraud.conditions.Condition;

public class DefaultConditionalAction<T> implements ConditionalAction<T> {

    private final Condition condition;

    private final Action<T> action;

    private final ExecutionCondition executionCondition;
    
    private boolean canBeExecuted;

    public DefaultConditionalAction(
            final Condition condition, final Action<T> action, final ExecutionCondition executionCondition) {
        this.condition = condition;
        this.action = action;
        this.executionCondition = executionCondition;
        canBeExecuted = false;
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
        canBeExecuted = canBeExecutedInCaseOfFulfillment() || canBeExecutedInCaseOfViolation();
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
        if (canBeExecuted) {
            return action.execute();
        } else {
            throw new IllegalStateException(
                    "Can't execute conditional the action " + this + " because the condition isn't appropriate");
        }
    }
}
