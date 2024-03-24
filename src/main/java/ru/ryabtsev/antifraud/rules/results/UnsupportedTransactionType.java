package ru.ryabtsev.antifraud.rules.results;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;

public class UnsupportedTransactionType extends RuleIsNotApplied {

    private static final String DEFAULT_MESSAGE = "Правило не применяется для транзакция такого типа";

    public UnsupportedTransactionType(final Rule rule, final RuleConfiguration ruleConfiguration) {
        super(rule, ruleConfiguration, DEFAULT_MESSAGE);
    }
}
