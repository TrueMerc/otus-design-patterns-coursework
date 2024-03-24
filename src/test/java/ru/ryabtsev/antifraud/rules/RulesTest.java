package ru.ryabtsev.antifraud.rules;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ryabtsev.antifraud.caches.BlackTinCache;
import ru.ryabtsev.antifraud.caches.BlackTinMockCache;
import ru.ryabtsev.antifraud.caches.GrayCardNumberCache;
import ru.ryabtsev.antifraud.caches.GrayCardNumberMockCache;
import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberCache;
import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberMockCache;
import ru.ryabtsev.antifraud.caches.TrustedCardNumberCache;
import ru.ryabtsev.antifraud.caches.TrustedCardNumberMockCache;
import ru.ryabtsev.antifraud.quarantine.QuarantinedEntity;
import ru.ryabtsev.antifraud.rules.black.BlackRule;
import ru.ryabtsev.antifraud.rules.black.RecipientInBlackLists;
import ru.ryabtsev.antifraud.rules.configurations.NewPayeeRuleConfiguration;
import ru.ryabtsev.antifraud.rules.gray.GrayBeneficiary;
import ru.ryabtsev.antifraud.rules.gray.GrayRule;
import ru.ryabtsev.antifraud.rules.gray.NewPayee;
import ru.ryabtsev.antifraud.rules.results.RuleIsApplied;
import ru.ryabtsev.antifraud.rules.results.RuleIsNotApplied;
import ru.ryabtsev.antifraud.rules.results.UnsupportedTransactionType;
import ru.ryabtsev.antifraud.rules.white.TrustedTransfer;
import ru.ryabtsev.antifraud.rules.white.WhiteRule;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.rbs.C2BPayment;
import ru.ryabtsev.antifraud.transactions.rbs.TransferToAnotherCard;

class RulesTest {

    private static final String TIN_FROM_BLACK_LIST = "00000000001";


    private final BlackTinCache blackTinCache = new BlackTinMockCache(
            List.of("00000000000", TIN_FROM_BLACK_LIST, "00000000002")
    );
    private final Rule recipientInBlackLists = new RecipientInBlackLists(null, blackTinCache);

    private static final String CARD_NUMBER_FROM_WHITE_LIST = "1234567890000001";

    private static final String CARD_NUMBER_OUT_OF_WHITE_LIST = "1234567890000004";

    private final TrustedCardNumberCache trustedCardNumberCache = new TrustedCardNumberMockCache(
            List.of(CARD_NUMBER_FROM_WHITE_LIST, "1234567890000002", "1234567890000003")
    );

    private final Rule trustedTransfer = new TrustedTransfer(null, trustedCardNumberCache);

    private static final String CARD_NUMBER_FROM_GRAY_LIST = "1234567890000001";

    private static final String CARD_NUMBER_OUT_OF_GRAY_LIST = "1234567890000004";

    private final GrayCardNumberCache grayCardNumberCache = new GrayCardNumberMockCache(
            List.of(CARD_NUMBER_FROM_GRAY_LIST, "1234567890000002", "1234567890000003")
    );

    private final Rule grayBeneficiary = new GrayBeneficiary(null, grayCardNumberCache);

    private static final String CARD_NUMBER_FROM_QUARANTINE = "1234567890000001";

    private static final String NEW_CARD_NUMBER_FROM_QUARANTINE = "1234567890000005";

    private static final String CARD_NUMBER_OUT_OF_LISTS = "1234567890000006";

    private final QuarantinedCardNumberCache quarantinedCardNumberCache = new QuarantinedCardNumberMockCache(
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

    private final NewPayeeRuleConfiguration ruleConfiguration = new NewPayeeRuleConfiguration(Duration.ofMinutes(2));

    private final Rule newPayee = new NewPayee(ruleConfiguration, trustedCardNumberCache, quarantinedCardNumberCache);

    private final Rules rules = new Rules(
            List.of(newPayee, grayBeneficiary, trustedTransfer, recipientInBlackLists),
            new BlackWhiteGrayExecutionOrder()
    );

    @Test
    void shouldBeProcessedByBlackRule() {
        // Arrange:
        final Transaction transaction = C2BPayment.builder()
                .withPayeeTin(TIN_FROM_BLACK_LIST)
                .build();

        // Act:
        final var processedTransaction = rules.applyTo(transaction);

        // Assert:
        Assertions.assertTrue(processedTransaction.isProcessed());
        final var ruleExecutionResults = processedTransaction.getRuleExecutionResults();
        Assertions.assertEquals(1, ruleExecutionResults.size());
        final var ruleExecutionResult = ruleExecutionResults.get(0);
        Assertions.assertTrue(ruleExecutionResult.isIncident());
    }

    @Test
    void shouldBeProcessedByWhiteRule() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withDestinationCardNumber(CARD_NUMBER_FROM_WHITE_LIST)
                .build();

        // Act:
        final var processedTransaction = rules.applyTo(transaction);

        // Assert:
        Assertions.assertTrue(processedTransaction.isProcessed());
        final var ruleExecutionResults = processedTransaction.getRuleExecutionResults();
        Assertions.assertEquals(2, ruleExecutionResults.size());
        final var blackRuleExecutionResult = ruleExecutionResults.get(0);
        Assertions.assertTrue(blackRuleExecutionResult.getRule() instanceof BlackRule);
        Assertions.assertFalse(blackRuleExecutionResult.isIncident());
        Assertions.assertEquals(UnsupportedTransactionType.STATUS, blackRuleExecutionResult.getStatus());
        Assertions.assertEquals(UnsupportedTransactionType.DEFAULT_MESSAGE, blackRuleExecutionResult.getMessage());
        final var whiteRuleExecutionResult = ruleExecutionResults.get(1);
        Assertions.assertTrue(whiteRuleExecutionResult.getRule() instanceof WhiteRule);
        Assertions.assertFalse(whiteRuleExecutionResult.isIncident());
        Assertions.assertEquals(RuleIsApplied.STATUS, whiteRuleExecutionResult.getStatus());
    }

    @Test
    void shouldBeProcessedByAllRules() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withDestinationCardNumber(CARD_NUMBER_OUT_OF_LISTS)
                .build();

        // Act:
        final var processedTransaction = rules.applyTo(transaction);

        // Assert:
        Assertions.assertTrue(processedTransaction.isProcessed());
        final var ruleExecutionResults = processedTransaction.getRuleExecutionResults();
        Assertions.assertEquals(4, ruleExecutionResults.size());
        final var blackRuleExecutionResult = ruleExecutionResults.get(0);
        Assertions.assertTrue(blackRuleExecutionResult.getRule() instanceof BlackRule);
        Assertions.assertFalse(blackRuleExecutionResult.isIncident());
        Assertions.assertEquals(UnsupportedTransactionType.STATUS, blackRuleExecutionResult.getStatus());
        Assertions.assertEquals(UnsupportedTransactionType.DEFAULT_MESSAGE, blackRuleExecutionResult.getMessage());
        final var whiteRuleExecutionResult = ruleExecutionResults.get(1);
        Assertions.assertTrue(whiteRuleExecutionResult.getRule() instanceof WhiteRule);
        Assertions.assertFalse(whiteRuleExecutionResult.isIncident());
        Assertions.assertEquals(RuleIsNotApplied.STATUS, whiteRuleExecutionResult.getStatus());
        final var grayRuleOneExecutionResult = ruleExecutionResults.get(2);
        Assertions.assertTrue(grayRuleOneExecutionResult.getRule() instanceof GrayRule);
        Assertions.assertTrue(grayRuleOneExecutionResult.isIncident());
        Assertions.assertEquals(RuleIsApplied.STATUS, grayRuleOneExecutionResult.getStatus());
        final var grayRuleTwoExecutionResult = ruleExecutionResults.get(3);
        Assertions.assertTrue(grayRuleTwoExecutionResult.getRule() instanceof GrayRule);
        Assertions.assertFalse(grayRuleTwoExecutionResult.isIncident());
        Assertions.assertEquals(RuleIsNotApplied.STATUS, grayRuleTwoExecutionResult.getStatus());
    }
}