package ru.ryabtsev.antifraud.rules.conditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberCache;
import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberMockCache;
import ru.ryabtsev.antifraud.quarantine.QuarantinedEntity;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.rbs.TransferToAnotherCard;
import ru.ryabtsev.antifraud.transactions.traits.DestinationCardNumberContainer;

class CardNumberIsNotPresentInQuarantineTest {

    private static final String CARD_NUMBER_FROM_QUARANTINE = "1234567890000001";

    private static final String CARD_NUMBER_OUT_OF_QUARANTINE = "1234567890000004";

    private QuarantinedCardNumberCache cache = new QuarantinedCardNumberMockCache(
            List.of(
                    new QuarantinedEntity<>(CARD_NUMBER_FROM_QUARANTINE, Duration.ZERO),
                    new QuarantinedEntity<>("1234567890000002", Duration.ZERO),
                    new QuarantinedEntity<>("1234567890000003", Duration.ZERO)
            )
    );

    @Test
    void shouldPresentInQuarantine() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withDestinationCardNumber(CARD_NUMBER_FROM_QUARANTINE)
                .build();
        final var accessor = new PropertyAccessor<>(
                transaction, DestinationCardNumberContainer::getDestinationCardNumber);
        final var condition = new CardNumberIsNotPresentInQuarantine(accessor, cache);

        // Assert:
        assertTrue(condition.isViolated());
    }

    @Test
    void shouldNotPresentInQuarantine() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withDestinationCardNumber(CARD_NUMBER_OUT_OF_QUARANTINE)
                .build();
        final var accessor = new PropertyAccessor<>(
                transaction, DestinationCardNumberContainer::getDestinationCardNumber);
        final var condition = new CardNumberIsNotPresentInQuarantine(accessor, cache);

        // Assert:
        assertTrue(condition.isFulfilled());
    }
}