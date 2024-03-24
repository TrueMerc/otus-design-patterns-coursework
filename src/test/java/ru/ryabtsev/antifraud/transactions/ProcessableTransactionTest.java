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
        final ProcessableTransaction processedTransaction = ProcessableTransaction.ofFinal(
                ProcessableTransaction.ofPartial(
                        ProcessableTransaction.ofPartial(transaction)
                )
        );
        final ProcessableTransaction unprocessedTransaction = ProcessableTransaction.ofPartial(
                ProcessableTransaction.ofPartial(
                        ProcessableTransaction.ofPartial(transaction)
                )
        );
        // Assert:
        assertTrue(processedTransaction.isProcessed());
        assertFalse(unprocessedTransaction.isProcessed());
    }

    @Test
    void alreadyProcessedTransactionProcessingAttempt() {
        final ProcessableTransaction processableTransaction = ProcessableTransaction.ofFinal(transaction);
        assertThrows(IllegalArgumentException.class, () -> ProcessableTransaction.ofFinal(processableTransaction));
    }


}