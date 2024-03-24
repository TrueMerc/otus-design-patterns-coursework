package ru.ryabtsev.antifraud.rules;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ryabtsev.antifraud.rules.black.BlackRule;
import ru.ryabtsev.antifraud.rules.gray.GrayRule;
import ru.ryabtsev.antifraud.rules.white.WhiteRule;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;

class BlackWhiteGrayExecutionOrderTest {

    @Test
    void shouldBeInRightOrder() {
        // Arrange:
        final var rules = List.of(new DummyGrayRule(), new DummyBlackRule(), new DummyWhiteRule());
        final var executionOrder = new BlackWhiteGrayExecutionOrder();

        // Act:
        final var sortedRules = executionOrder.applyTo(rules);

        // Assert:
        assertEquals(rules.size(), sortedRules.size());
        assertTrue(sortedRules.get(0) instanceof BlackRule);
        assertTrue(sortedRules.get(1) instanceof WhiteRule);
        assertTrue(sortedRules.get(2) instanceof GrayRule);
    }

    private static class DummyBlackRule implements BlackRule {
        @Override
        public ProcessableTransaction applyTo(final Transaction transaction) {
            return ProcessableTransaction.ofFinal(transaction);
        }
    }

    private static class DummyWhiteRule implements WhiteRule {
        @Override
        public ProcessableTransaction applyTo(final Transaction transaction) {
            return ProcessableTransaction.ofFinal(transaction);
        }
    }

    private static class DummyGrayRule implements GrayRule {
        @Override
        public ProcessableTransaction applyTo(final Transaction transaction) {
            return ProcessableTransaction.ofPartial(transaction);
        }
    }
}