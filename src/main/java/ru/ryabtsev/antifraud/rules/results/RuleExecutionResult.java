package ru.ryabtsev.antifraud.rules.results;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;

public interface RuleExecutionResult {

    Rule getRule();

    RuleConfiguration getRuleConfiguration();

    String getStatus();

    default boolean isIncident() {
        return false;
    }
}
