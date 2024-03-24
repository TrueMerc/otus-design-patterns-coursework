package ru.ryabtsev.antifraud.rules.results.incidents;

import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.results.RuleIsApplied;

public class BasicIncident extends RuleIsApplied implements Incident {

    public BasicIncident(final Rule rule, final RuleConfiguration ruleConfiguration, final String message) {
        super(rule, ruleConfiguration, message);
    }

    @Override
    public boolean isIncident() {
        return Incident.super.isIncident();
    }
}
