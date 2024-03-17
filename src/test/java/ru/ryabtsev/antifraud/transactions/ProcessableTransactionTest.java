package ru.ryabtsev.antifraud.transactions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProcessableTransactionTest {

    private final Transaction transaction = new DummyTransaction("New", "");

    @Test
    void partiallyProcessedTransactionTest() {
        // Arrange:
        final ProcessableTransaction processableTransaction = ProcessableTransaction.ofPartial(transaction);
        // Assert:
        assertFalse(processableTransaction.isProcessed());

    }

    @Test
    void finallyProcessedTransactionTest() {
        // Arrange:
        final ProcessableTransaction processableTransaction = ProcessableTransaction.ofFinal(transaction);
        // Assert:
        assertTrue(processableTransaction.isProcessed());
    }

    @Test
    void transactionChainTest() {
        // Arrange:
        final ProcessableTransaction processableTransaction = ProcessableTransaction.ofFinal(
                ProcessableTransaction.ofPartial(
                        ProcessableTransaction.ofPartial(transaction)
                )
        );
        // Assert:
        assertTrue(processableTransaction.isProcessed());
    }

    @Test
    void alreadyProcessedTransactionProcessingAttempt() {
        final ProcessableTransaction processableTransaction = ProcessableTransaction.ofFinal(transaction);
        assertThrows(IllegalArgumentException.class, () -> ProcessableTransaction.ofFinal(processableTransaction));
    }


}