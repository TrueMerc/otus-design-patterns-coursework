package ru.ryabtsev.antifraud.rules;

import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;

public class DummyRule implements Rule {

    private final RuleExecutionResult ruleExecutionResult;


    public DummyRule(final RuleExecutionResult ruleExecutionResult) {
        this.ruleExecutionResult = ruleExecutionResult;
    }

    @Override
    public Transaction applyTo(final Transaction transaction) {
        return ProcessableTransaction.ofPartial(transaction, ruleExecutionResult);
    }
}
