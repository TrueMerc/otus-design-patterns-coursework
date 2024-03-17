package ru.ryabtsev.antifraud.transactions;

/**
 * The interface for all transactions in the application.
 */
public interface Transaction {

    /**
     * Returns the transaction's current status.
     * @return the transaction's current status.
     */
    String getStatus();

    /**
     * Returns the resolution that was made for this transaction.
     * @return the resolution that was made for this transaction.
     */
    String getResolution();
}
