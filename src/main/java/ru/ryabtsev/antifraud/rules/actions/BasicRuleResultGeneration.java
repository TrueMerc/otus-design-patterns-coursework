package ru.ryabtsev.antifraud.rules.actions;

import ru.ryabtsev.antifraud.conditional.actions.Action;
import ru.ryabtsev.antifraud.rules.BasicRuleExecutionResult;
import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.RuleExecutionResult;

public class BasicRuleResultGeneration implements Action<RuleExecutionResult> {

    private final Rule rule;

    private final RuleConfiguration ruleConfiguration;

    private final String status;

    public BasicRuleResultGeneration(final Rule rule, final RuleConfiguration ruleConfiguration, final String status) {
        this.rule = rule;
        this.ruleConfiguration = ruleConfiguration;
        this.status = status;
    }

    @Override
    public RuleExecutionResult execute() {
        return new BasicRuleExecutionResult(rule, ruleConfiguration, status);
    }
}
