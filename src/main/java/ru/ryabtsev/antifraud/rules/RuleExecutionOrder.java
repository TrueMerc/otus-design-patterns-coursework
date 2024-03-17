package ru.ryabtsev.antifraud.rules;

import java.util.List;
import ru.ryabtsev.antifraud.traits.Applicable;

public interface RuleExecutionOrder extends Applicable<List<Rule>, List<Rule>> {
}
