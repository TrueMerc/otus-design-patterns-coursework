package ru.ryabtsev.antifraud.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import ru.ryabtsev.antifraud.rules.black.BlackRule;
import ru.ryabtsev.antifraud.rules.gray.GrayRule;
import ru.ryabtsev.antifraud.rules.white.WhiteRule;

public class BlackWhiteGrayExecutionOrder implements RuleExecutionOrder {

    @Override
    public List<Rule> applyTo(final List<Rule> rules) {
        final List<Rule> sortedRules = new ArrayList<>(rules);
        Collections.sort(sortedRules, new BlackWhiteGrayRulesComparator());
        return sortedRules;
    }

    private static class BlackWhiteGrayRulesComparator implements Comparator<Rule> {

        private final Map<Class<? extends Rule>, Integer> ruleTypeWeights = Map.of(
                BlackRule.class, 1,
                WhiteRule.class, 2,
                GrayRule.class, 3
        );

        @Override
        public int compare(final Rule ruleOne, final Rule ruleTwo) {
            return getWeight(ruleOne) - getWeight(ruleTwo);
        }

        private int getWeight(final Rule rule) {
            return ruleTypeWeights.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().isAssignableFrom(rule.getClass()))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElseThrow(() -> new IllegalArgumentException("Rule of unsupported type " + rule.getClass()));
        }
    }
}
