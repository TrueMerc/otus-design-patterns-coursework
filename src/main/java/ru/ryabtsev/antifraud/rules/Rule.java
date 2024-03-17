package ru.ryabtsev.antifraud.rules;

import ru.ryabtsev.antifraud.traits.Applicable;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;

public interface Rule extends Applicable<Transaction, ProcessableTransaction> {
}
