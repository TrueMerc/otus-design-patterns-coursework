package ru.ryabtsev.antifraud.rules.actions;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.results.RuleIsNotApplied;

public class RuleIsNotAppliedGeneration extends BasicRuleResultGeneration {

    public RuleIsNotAppliedGeneration(final Rule rule,
                                      final RuleConfiguration ruleConfiguration,
                                      final String message) {
        super(new RuleIsNotApplied(rule, ruleConfiguration, message));
    }
}
