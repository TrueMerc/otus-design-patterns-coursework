package ru.ryabtsev.antifraud.rules.configurations;

import java.time.Duration;
import lombok.Getter;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;

@Getter
public class NewPayeeRuleConfiguration implements RuleConfiguration {

    private final Duration quarantineThreshold;

    public NewPayeeRuleConfiguration(Duration quarantineThreshold) {
        this.quarantineThreshold = quarantineThreshold;
    }
}
