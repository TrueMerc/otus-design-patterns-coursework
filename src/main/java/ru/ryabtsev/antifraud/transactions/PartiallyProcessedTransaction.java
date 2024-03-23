package ru.ryabtsev.antifraud.transactions;

import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;

class PartiallyProcessedTransaction extends ProcessableTransaction {

    PartiallyProcessedTransaction(final Transaction transaction, final RuleExecutionResult result) {
        super(transaction, result);
    }

    PartiallyProcessedTransaction(final Transaction transaction) {
        super(transaction);
    }

    @Override
    public boolean isProcessed() {
        return false;
    }
}
