package ru.ryabtsev.antifraud.rules.conditions;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberCache;
import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberMockCache;
import ru.ryabtsev.antifraud.quarantine.QuarantinedEntity;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.rbs.TransferToAnotherCard;
import ru.ryabtsev.antifraud.transactions.traits.DestinationCardNumberContainer;

class CardNumberPresentsInQuarantineForLongTimeTest {

    private static final String CARD_NUMBER_FROM_QUARANTINE = "1234567890000001";

    private QuarantinedCardNumberCache cache = new QuarantinedCardNumberMockCache(
            List.of(
                    new QuarantinedEntity<>(
                            CARD_NUMBER_FROM_QUARANTINE,
                            LocalDateTime.now().minus(Duration.ofMinutes(5)),
                            Duration.ZERO
                    ),
                    new QuarantinedEntity<>("1234567890000002", Duration.ZERO),
                    new QuarantinedEntity<>("1234567890000003", Duration.ZERO)
            )
    );

    @Test
    void shouldBeInQuarantineForLongTime() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withDestinationCardNumber(CARD_NUMBER_FROM_QUARANTINE)
                .build();

        final var accessor = new PropertyAccessor<>(
                transaction, DestinationCardNumberContainer::getDestinationCardNumber);
        final Duration threshold = Duration.ofMinutes(4);
        final var condition = new CardNumberPresentsInQuarantineForLongTime(accessor, cache, threshold);

        // Assert:
        assertTrue(condition.isFulfilled());
    }

    @Test
    void shouldNotBeInQuarantineForLongTime() {
        // Arrange:
        final Transaction transaction = TransferToAnotherCard.builder()
                .withDestinationCardNumber(CARD_NUMBER_FROM_QUARANTINE)
                .build();

        final var accessor = new PropertyAccessor<>(
                transaction, DestinationCardNumberContainer::getDestinationCardNumber);
        final Duration threshold = Duration.ofMinutes(6);
        final var condition = new CardNumberPresentsInQuarantineForLongTime(accessor, cache, threshold);

        // Assert:
        assertTrue(condition.isViolated());
    }
}