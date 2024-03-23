package ru.ryabtsev.antifraud.rules;

import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ryabtsev.antifraud.BlackTinMockCache;
import ru.ryabtsev.antifraud.caches.BlackTinCache;
import ru.ryabtsev.antifraud.rules.black.RecipientInBlackLists;
import ru.ryabtsev.antifraud.rules.incidents.Incident;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.rbs.C2BPayment;

class RecipientInBlackListsTest {

    private static final String TIN_FROM_BLACK_LIST = "00000000001";

    private static final String TIN_OUT_OF_BLACK_LIST = "00000000004";

    private final BlackTinCache blackTinCache = new BlackTinMockCache(
            List.of("00000000000", TIN_FROM_BLACK_LIST, "00000000002")
    );

    private final Rule rule = new RecipientInBlackLists(null, blackTinCache);

    @Test
    void shouldBeInBlackList() {
        // Arrange:
        final Transaction transaction = C2BPayment.builder()
                .withId(Instancio.create(Long.class))
                .withPayeeTin(TIN_FROM_BLACK_LIST)
                .build();

        // Act:
        final ProcessableTransaction processedTransaction = rule.applyTo(transaction);

        // Assert:
        Assertions.assertTrue(processedTransaction.isProcessed());
        final List<RuleExecutionResult> results = processedTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, results.size());
        final RuleExecutionResult result = results.get(0);
        Assertions.assertTrue(result instanceof Incident);
        Assertions.assertTrue(result.isIncident());
    }

    @Test
    void shouldNotBeInBlackList() {
        // Arrange:
        final Transaction transaction = C2BPayment.builder()
                .withPayeeTin(TIN_OUT_OF_BLACK_LIST)
                .build();

        // Act:
        final ProcessableTransaction processedTransaction = rule.applyTo(transaction);
        // Assert:
        Assertions.assertFalse(processedTransaction.isProcessed());
        final List<RuleExecutionResult> results = processedTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, results.size());
        final RuleExecutionResult result = results.get(0);
        Assertions.assertFalse(result instanceof Incident);
        Assertions.assertFalse(result.isIncident());
    }
}