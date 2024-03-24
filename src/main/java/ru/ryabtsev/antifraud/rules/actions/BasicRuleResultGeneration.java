package ru.ryabtsev.antifraud.rules.actions;

import java.util.Objects;
import ru.ryabtsev.antifraud.conditional.actions.Action;
import ru.ryabtsev.antifraud.rules.results.BasicRuleExecutionResult;
import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;

public class BasicRuleResultGeneration implements Action<RuleExecutionResult> {

    private final Rule rule;

    private final RuleConfiguration ruleConfiguration;

    private final String status;

    private final String message;

    private final RuleExecutionResult predefinedResult;

    public BasicRuleResultGeneration(
            final Rule rule, final RuleConfiguration ruleConfiguration, final String status, final String message) {
        this.rule = rule;
        this.ruleConfiguration = ruleConfiguration;
        this.status = status;
        this.message = message;
        predefinedResult = null;
    }

    public BasicRuleResultGeneration(final RuleExecutionResult predefinedResult) {
        rule = null;
        ruleConfiguration = null;
        status = null;
        message = null;
        this.predefinedResult = predefinedResult;
    }

    @Override
    public RuleExecutionResult execute() {
        return Objects.requireNonNullElseGet(
                predefinedResult, () -> new BasicRuleExecutionResult(rule, ruleConfiguration, status, message));
    }
}
