package ru.ryabtsev.antifraud.rules.results.incidents;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;

public class BasicIncident implements Incident {

    private final Rule rule;

    private final RuleConfiguration ruleConfiguration;

    private final String status;

    public BasicIncident(final Rule rule, final RuleConfiguration ruleConfiguration, final String message) {
        this.rule = rule;
        this.ruleConfiguration = ruleConfiguration;
        this.status = message;
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    @Override
    public RuleConfiguration getRuleConfiguration() {
        return ruleConfiguration;
    }

    @Override
    public String getMessage() {
        return status;
    }
}
