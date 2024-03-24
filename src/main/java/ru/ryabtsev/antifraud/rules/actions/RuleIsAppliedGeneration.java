package ru.ryabtsev.antifraud.rules.actions;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;
import ru.ryabtsev.antifraud.rules.results.RuleIsApplied;

public class RuleIsAppliedGeneration extends BasicRuleResultGeneration {

    public RuleIsAppliedGeneration(final Rule rule, final RuleConfiguration ruleConfiguration, final String message) {
        super(new RuleIsApplied(rule, ruleConfiguration, message));
    }
}
