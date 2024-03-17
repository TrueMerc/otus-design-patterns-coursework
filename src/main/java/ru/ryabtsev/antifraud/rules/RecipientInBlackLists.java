package ru.ryabtsev.antifraud.rules;

import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;

/**
 * The rule that checks if the recipient requisites are present in one of black lists.
 * This
 */
public class RecipientInBlackLists implements BlackRule {
    @Override
    public Transaction applyTo(final Transaction transaction) {
        return ProcessableTransaction.ofFinal(transaction);
    }
}
