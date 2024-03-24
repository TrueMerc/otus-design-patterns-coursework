package ru.ryabtsev.antifraud.rules;

import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;

/**
 * The special implementation of rule interface that has the only one purpose to mark transaction as processed.
 */
public class FinishRule implements Rule {
    @Override
    public ProcessableTransaction applyTo(final Transaction transaction) {
        return ProcessableTransaction.ofFinal(transaction);
    }
}
