package ru.ryabtsev.antifraud.transactions.traits;

/**
 * The common interface for all transactions which contain TIN (Taxpayer Identification Number) of a payee.
 */
public interface PayeeTinContainer {

    String getPayeeTin();
}
