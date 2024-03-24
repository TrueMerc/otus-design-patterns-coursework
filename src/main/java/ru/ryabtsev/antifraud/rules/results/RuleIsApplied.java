package ru.ryabtsev.antifraud.rules.results;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;

public class RuleIsApplied extends BasicRuleExecutionResult {
    public static final String STATUS = "TRIGGERED";

    public RuleIsApplied(final Rule rule, final RuleConfiguration ruleConfiguration, final String message) {
        super(rule, ruleConfiguration, STATUS, message);
    }
}
