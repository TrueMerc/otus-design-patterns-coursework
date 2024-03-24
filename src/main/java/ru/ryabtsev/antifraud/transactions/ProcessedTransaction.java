package ru.ryabtsev.antifraud.transactions;

import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;

class ProcessedTransaction extends ProcessableTransaction {

    ProcessedTransaction(final Transaction transaction, final RuleExecutionResult result) {
        super(transaction, result);
    }

    ProcessedTransaction(final Transaction transaction) {
        super(transaction);
    }

    ProcessedTransaction(final ProcessableTransaction processableTransaction) {
        super(processableTransaction);
    }

    @Override
    public boolean isProcessed() {
        return true;
    }
}
