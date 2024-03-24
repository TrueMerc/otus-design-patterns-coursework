package ru.ryabtsev.antifraud.rules.results;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;

public class RuleIsNotApplied extends BasicRuleExecutionResult {
    public static final String STATUS = "NOT TRIGGERED";

    public RuleIsNotApplied(final Rule rule, final RuleConfiguration ruleConfiguration, final String message) {
        super(rule, ruleConfiguration, STATUS, message);
    }
}
