package ru.ryabtsev.antifraud.rules;

public interface RuleExecutionResult {

    Rule getRule();

    RuleConfiguration getRuleConfiguration();

    String getStatus();

    default boolean isIncident() {
        return false;
    }
}
