package ru.ryabtsev.antifraud.transactions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProcessableTransactionTest {

    private final Transaction transaction = new DummyTransaction("New", "");

    @Test
    void partiallyProcessedTransactionTest() {
        final ProcessableTransaction processableTransaction = ProcessableTransaction.ofPartial(transaction);
        assertFalse(processableTransaction.isProcessed());

    }

    @Test
    void finallyProcessedTransactionTest() {
        final ProcessableTransaction processableTransaction = ProcessableTransaction.ofFinal(transaction);
        assertTrue(processableTransaction.isProcessed());
    }


}