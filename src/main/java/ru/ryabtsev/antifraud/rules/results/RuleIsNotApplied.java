package ru.ryabtsev.antifraud.rules.results;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;

public class RuleIsNotApplied extends BasicRuleExecutionResult {
    private static final String STATUS_MESSAGE = "Правило не применилось";

    public RuleIsNotApplied(final Rule rule, final RuleConfiguration ruleConfiguration) {
        super(rule, ruleConfiguration, STATUS_MESSAGE);
    }

    @Override
    public String getMessage() {
        return STATUS_MESSAGE;
    }
}
