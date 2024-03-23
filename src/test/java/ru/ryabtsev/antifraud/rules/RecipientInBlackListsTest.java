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

    private final String TIN = "00000000001";

    private final BlackTinCache blackTinCache = new BlackTinMockCache(
            List.of("00000000000", TIN, "00000000002")
    );

    private final Rule rule = new RecipientInBlackLists(blackTinCache);

    @Test
    void shouldBeInBlackList() {
        // Arrange:
        final Transaction transaction = C2BPayment.builder()
                .withId(Instancio.create(Long.class))
                .withPayeeTin(TIN)
                .build();

        // Act:
        final ProcessableTransaction processedTransaction = rule.applyTo(transaction);

        // Assert:
        Assertions.assertTrue(processedTransaction.isProcessed());
        final List<RuleExecutionResult> results = processedTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, results.size());
        final RuleExecutionResult result = results.get(0);
        Assertions.assertTrue(result instanceof Incident);
    }


}