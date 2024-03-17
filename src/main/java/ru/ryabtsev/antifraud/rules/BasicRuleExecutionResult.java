package ru.ryabtsev.antifraud.rules;

public class BasicRuleExecutionResult implements RuleExecutionResult {

    private final Rule rule;

    private final RuleConfiguration ruleConfiguration;

    private final String status;

    public BasicRuleExecutionResult(
            final Rule rule,
            final RuleConfiguration ruleConfiguration,
            final String status) {
        this.rule = rule;
        this.ruleConfiguration = ruleConfiguration;
        this.status = status;
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    @Override
    public RuleConfiguration getRuleConfiguration() {
        return ruleConfiguration;
    }

    @Override
    public String getStatus() {
        return status;
    }
}
