package ru.ryabtsev.antifraud.rules.actions;

import ru.ryabtsev.antifraud.conditional.actions.Action;
import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;
import ru.ryabtsev.antifraud.rules.results.incidents.BasicIncident;

public class BasicIncidentGeneration implements Action<RuleExecutionResult> {

    private final Rule rule;

    private final RuleConfiguration ruleConfiguration;

    private final String message;

    public BasicIncidentGeneration(final Rule rule, final RuleConfiguration ruleConfiguration, final String message) {
        this.rule = rule;
        this.ruleConfiguration = ruleConfiguration;
        this.message = message;
    }

    @Override
    public RuleExecutionResult execute() {
        return new BasicIncident(rule, ruleConfiguration, message);
    }
}
