package ru.ryabtsev.antifraud.rules.actions;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.results.UnsupportedTransactionType;

public class UnsupportedTransactionTypeGeneration extends BasicRuleResultGeneration {

    public UnsupportedTransactionTypeGeneration(final Rule rule, RuleConfiguration ruleConfiguration) {
        super(new UnsupportedTransactionType(rule, ruleConfiguration));
    }
}
