package ru.ryabtsev.antifraud.rules.gray;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberCache;
import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberMockCache;
import ru.ryabtsev.antifraud.caches.TrustedCardNumberCache;
import ru.ryabtsev.antifraud.caches.TrustedCardNumberMockCache;
import ru.ryabtsev.antifraud.quarantine.QuarantinedEntity;
import ru.ryabtsev.antifraud.rules.Rule;
import ru.ryabtsev.antifraud.rules.configurations.NewPayeeRuleConfiguration;
import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;
import ru.ryabtsev.antifraud.rules.results.RuleIsApplied;
import ru.ryabtsev.antifraud.rules.results.RuleIsNotApplied;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.rbs.TransferToAnotherCard;

class NewPayeeTest {

    private static final String CARD_NUMBER_FROM_QUARANTINE = "1234567890000001";

    private static final String CARD_NUMBER_FROM_WHITE_LIST = "1234567890000004";

    private static final String NEW_CARD_NUMBER_FROM_QUARANTINE = "1234567890000005";

    private static final String CARD_NUMBER_OUT_OF_LISTS = "1234567890000006";

    private QuarantinedCardNumberCache quarantinedCardNumberCache = new QuarantinedCardNumberMockCache(
            List.of(
                    new QuarantinedEntity<>(
                            CARD_NUMBER_FROM_QUARANTINE,
                            LocalDateTime.now().minus(Duration.ofMinutes(5)),
                            Duration.ZERO
                    ),
                    new QuarantinedEntity<>("1234567890000002", Duration.ZERO),
                    new QuarantinedEntity<>("1234567890000003", Duration.ZERO),
                    new QuarantinedEntity<>(NEW_CARD_NUMBER_FROM_QUARANTINE, Duration.ofMinutes(1))
            )
    );

    private final TrustedCardNumberCache trustedCardNumberCache = new TrustedCardNumberMockCache(
            List.of(CARD_NUMBER_FROM_WHITE_LIST)
    );

    private final NewPayeeRuleConfiguration ruleConfiguration = new NewPayeeRuleConfiguration(Duration.ofMinutes(2));

    private final Rule rule = new NewPayee(ruleConfiguration, trustedCardNumberCache, quarantinedCardNumberCache);

    @Test
    void shouldBeInWhitelist() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withDestinationCardNumber(CARD_NUMBER_FROM_WHITE_LIST)
                .build();

        // Act:
        final var processableTransaction = rule.applyTo(transaction);

        // Assert:
        Assertions.assertFalse(processableTransaction.isProcessed());
        final List<RuleExecutionResult> results = processableTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, results.size());
        final RuleExecutionResult result = results.get(0);
        Assertions.assertFalse(result.isIncident());
        Assertions.assertEquals(RuleIsNotApplied.STATUS, result.getStatus());
        Assertions.assertEquals(NewPayee.PAYEE_IN_WHITE_LIST, result.getMessage());
    }

    @Test
    void shouldBeInQuarantineForLongTime() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withDestinationCardNumber(CARD_NUMBER_FROM_QUARANTINE)
                .build();

        // Act:
        final var processableTransaction = rule.applyTo(transaction);

        // Assert:
        Assertions.assertFalse(processableTransaction.isProcessed());
        final List<RuleExecutionResult> results = processableTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, results.size());
        final RuleExecutionResult result = results.get(0);
        Assertions.assertFalse(result.isIncident());
        Assertions.assertEquals(RuleIsNotApplied.STATUS, result.getStatus());
        Assertions.assertEquals(NewPayee.ALREADY_PRESENT_IN_QUARANTINE, result.getMessage());
    }

    @Test
    void shouldBeInQuarantineForShortTime() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withDestinationCardNumber(NEW_CARD_NUMBER_FROM_QUARANTINE)
                .build();

        // Act:
        final var processableTransaction = rule.applyTo(transaction);

        // Assert:
        Assertions.assertFalse(processableTransaction.isProcessed());
        final List<RuleExecutionResult> results = processableTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, results.size());
        final RuleExecutionResult result = results.get(0);
        Assertions.assertTrue(result.isIncident());
        Assertions.assertEquals(RuleIsApplied.STATUS, result.getStatus());
        Assertions.assertEquals(NewPayee.RECENTLY_PRESENT_IN_QUARANTINE, result.getMessage());
    }

    @Test
    void shouldNotBeInBothWhiteListAndQuarantine() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withDestinationCardNumber(CARD_NUMBER_OUT_OF_LISTS)
                .build();

        // Act:
        final var processableTransaction = rule.applyTo(transaction);

        // Assert:
        Assertions.assertFalse(processableTransaction.isProcessed());
        final List<RuleExecutionResult> results = processableTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, results.size());
        final RuleExecutionResult result = results.get(0);
        Assertions.assertTrue(result.isIncident());
        Assertions.assertEquals(RuleIsApplied.STATUS, result.getStatus());
        Assertions.assertEquals(NewPayee.NEW_PAYEE, result.getMessage());
    }
}