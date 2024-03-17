package ru.ryabtsev.antifraud.transactions;

import ru.ryabtsev.antifraud.rules.RuleExecutionResult;

class FinallyProcessedTransaction extends ProcessableTransaction {

    FinallyProcessedTransaction(final Transaction transaction, final RuleExecutionResult result) {
        super(transaction, result);
    }

    FinallyProcessedTransaction(final Transaction transaction) {
        super(transaction);
    }

    @Override
    public boolean isProcessed() {
        return true;
    }
}
