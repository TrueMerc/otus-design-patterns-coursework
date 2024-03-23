package ru.ryabtsev.antifraud.rules;

import java.util.List;
import ru.ryabtsev.antifraud.traits.Applicable;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;

public class Rules implements Applicable<Transaction, ProcessableTransaction> {

    private final List<Rule> rules;

    public Rules(final List<Rule> rules, final RuleExecutionOrder ruleExecutionOrder) {
        this.rules = ruleExecutionOrder.applyTo(rules);
    }

    public Rules(final List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public ProcessableTransaction applyTo(final Transaction transaction) {
        return ProcessableTransaction.ofFinal(transaction);
    }
}
