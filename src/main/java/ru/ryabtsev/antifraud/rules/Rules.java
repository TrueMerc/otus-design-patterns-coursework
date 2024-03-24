package ru.ryabtsev.antifraud.rules;

import java.util.ArrayList;
import java.util.List;
import ru.ryabtsev.antifraud.traits.Applicable;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;

public class Rules implements Applicable<Transaction, ProcessableTransaction> {

    private final List<Rule> rules;

    public Rules(final List<Rule> rules, final RuleExecutionOrder ruleExecutionOrder) {
        this.rules = ruleExecutionOrder.applyTo(rules);
        this.rules.add(new FinishRule());
    }

    public Rules(final List<Rule> rules) {
        this.rules = new ArrayList<>(rules);
        this.rules.add(new FinishRule());
    }

    @Override
    public ProcessableTransaction applyTo(final Transaction transaction) {
        ProcessableTransaction processableTransaction = ProcessableTransaction.ofPartial(transaction);
        for(final var rule : rules) {
            final var newProcessableTransaction = rule.applyTo(processableTransaction.getTransaction());
            final var ruleExecutionResults = newProcessableTransaction.getRuleExecutionResults();
            if (ruleExecutionResults.isEmpty()) {
                return ProcessableTransaction.ofFinal(processableTransaction);
            }
            final var newResult = ruleExecutionResults.get(0);
            processableTransaction = newProcessableTransaction.isProcessed()
                    ? ProcessableTransaction.ofFinal(processableTransaction, newResult)
                    : ProcessableTransaction.ofPartial(processableTransaction, newResult);
            if (processableTransaction.isProcessed()) {
                return processableTransaction;
            }
        }
        return processableTransaction;
    }
}
