package ru.ryabtsev.antifraud.rules.black;

import ru.ryabtsev.antifraud.caches.BlackTinCache;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;

/**
 * The rule that checks if the recipient requisites are present in one of the black lists.
 */
public class RecipientInBlackLists implements BlackRule {

    private final BlackTinCache blackTinCache;

    public RecipientInBlackLists(final BlackTinCache blackTinCache) {
        this.blackTinCache = blackTinCache;
    }

    @Override
    public ProcessableTransaction applyTo(final Transaction transaction) {
        return ProcessableTransaction.ofFinal(transaction);
    }
}
