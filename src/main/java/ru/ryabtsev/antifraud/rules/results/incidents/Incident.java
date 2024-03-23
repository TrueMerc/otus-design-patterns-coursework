package ru.ryabtsev.antifraud.rules.results.incidents;

import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;

public interface Incident extends RuleExecutionResult {

    @Override
    default boolean isIncident() {
        return true;
    }
}
