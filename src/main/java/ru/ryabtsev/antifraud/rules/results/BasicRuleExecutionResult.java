package ru.ryabtsev.antifraud.rules.results;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;

public class BasicRuleExecutionResult implements RuleExecutionResult {

    private static final String DEFAULT_STATUS = "NOT TRIGGERED";

    private final Rule rule;

    private final RuleConfiguration ruleConfiguration;

    private final String status;

    private final String message;

    public BasicRuleExecutionResult(
            final Rule rule,
            final RuleConfiguration ruleConfiguration,
            final String status,
            final String message) {
        this.rule = rule;
        this.ruleConfiguration = ruleConfiguration;
        this.status = status;
        this.message = message;
    }

    public BasicRuleExecutionResult(
            final Rule rule,
            final RuleConfiguration ruleConfiguration,
            final String message
    ) {
        this.rule = rule;
        this.ruleConfiguration = ruleConfiguration;
        status = DEFAULT_STATUS;
        this.message = message;
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

    @Override
    public String getMessage() {
        return message;
    }
}
