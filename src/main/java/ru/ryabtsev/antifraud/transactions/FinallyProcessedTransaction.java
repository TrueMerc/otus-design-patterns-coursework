package ru.ryabtsev.antifraud.transactions;

import java.util.List;
import ru.ryabtsev.antifraud.rules.RuleExecutionResult;

class FinallyProcessedTransaction extends ProcessableTransaction {

    FinallyProcessedTransaction(final Transaction transaction, final RuleExecutionResult result) {
        super(transaction, result);
    }

    FinallyProcessedTransaction(final Transaction transaction) {
        super(transaction);
    }

    FinallyProcessedTransaction(final ProcessableTransaction processableTransaction) {
        super(processableTransaction);
    }

    @Override
    public boolean isProcessed() {
        return true;
    }
}
