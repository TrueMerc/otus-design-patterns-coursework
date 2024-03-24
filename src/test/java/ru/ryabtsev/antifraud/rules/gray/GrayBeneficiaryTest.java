package ru.ryabtsev.antifraud.rules.gray;

import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ryabtsev.antifraud.caches.GrayCardNumberCache;
import ru.ryabtsev.antifraud.caches.GrayCardNumberMockCache;
import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;
import ru.ryabtsev.antifraud.rules.results.RuleIsApplied;
import ru.ryabtsev.antifraud.rules.results.RuleIsNotApplied;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.rbs.C2BPayment;
import ru.ryabtsev.antifraud.transactions.rbs.TransferToAnotherCard;

class GrayBeneficiaryTest {

    private static final String CARD_NUMBER_FROM_GRAY_LIST = "1234567890000001";

    private static final String CARD_NUMBER_OUT_OF_GRAY_LIST = "1234567890000004";

    private final GrayCardNumberCache grayCardNumberCache = new GrayCardNumberMockCache(
            List.of(CARD_NUMBER_FROM_GRAY_LIST, "1234567890000002", "1234567890000003")
    );

    private final Rule rule = new GrayBeneficiary(null, grayCardNumberCache);

    @Test
    void shouldBeApplied() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withId(Instancio.create(Long.class))
                .withDestinationCardNumber(CARD_NUMBER_FROM_GRAY_LIST)
                .build();

        // Act:
        final ProcessableTransaction processedTransaction = rule.applyTo(transaction);

        // Assert:
        Assertions.assertFalse(processedTransaction.isProcessed());
        final List<RuleExecutionResult> results = processedTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, results.size());
        final RuleExecutionResult result = results.get(0);
        Assertions.assertTrue(result.isIncident());
        Assertions.assertEquals(RuleIsApplied.STATUS, result.getStatus());
    }

    @Test
    void shouldNotBeApplied() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withId(Instancio.create(Long.class))
                .withDestinationCardNumber(CARD_NUMBER_OUT_OF_GRAY_LIST)
                .build();

        // Act:
        final ProcessableTransaction processedTransaction = rule.applyTo(transaction);

        // Assert:
        Assertions.assertFalse(processedTransaction.isProcessed());
        final List<RuleExecutionResult> results = processedTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, results.size());
        final RuleExecutionResult result = results.get(0);
        Assertions.assertFalse(result.isIncident());
        Assertions.assertEquals(RuleIsNotApplied.STATUS, result.getStatus());
    }

    @Test
    void shouldNotBeAppropriateType() {
        // Arrange:
        final Transaction transaction = C2BPayment.builder()
                .withId(Instancio.create(Long.class))
                .withPayeeTin(CARD_NUMBER_OUT_OF_GRAY_LIST)
                .build();

        // Act:
        final ProcessableTransaction processedTransaction = rule.applyTo(transaction);

        // Assert:
        Assertions.assertFalse(processedTransaction.isProcessed());
        final List<RuleExecutionResult> results = processedTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, results.size());
        final RuleExecutionResult result = results.get(0);
        Assertions.assertFalse(result.isIncident());
        Assertions.assertEquals(RuleIsNotApplied.STATUS, result.getStatus());
    }
}