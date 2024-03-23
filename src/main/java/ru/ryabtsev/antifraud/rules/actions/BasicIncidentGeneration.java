package ru.ryabtsev.antifraud.rules.actions;

import ru.ryabtsev.antifraud.conditional.actions.Action;
import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;
import ru.ryabtsev.antifraud.rules.results.incidents.BasicIncident;

public class BasicIncidentGeneration implements Action<RuleExecutionResult> {

    private final Rule rule;

    private final RuleConfiguration ruleConfiguration;

    private final String status;

    public BasicIncidentGeneration(Rule rule, RuleConfiguration ruleConfiguration, String status) {
        this.rule = rule;
        this.ruleConfiguration = ruleConfiguration;
        this.status = status;
    }

    @Override
    public RuleExecutionResult execute() {
        return new BasicIncident(rule, ruleConfiguration, status);
    }
}
