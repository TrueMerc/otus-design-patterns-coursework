package ru.ryabtsev.antifraud.transactions.traits;

/**
 * The common interface for all transactions which contain destination card number.
 */
public interface DestinationCardNumberContainer {

    String destinationCardNumber();
}
