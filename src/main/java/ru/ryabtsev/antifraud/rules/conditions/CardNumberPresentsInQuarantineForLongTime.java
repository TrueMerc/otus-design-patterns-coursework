package ru.ryabtsev.antifraud.rules.conditions;

import java.time.Duration;
import java.time.LocalDateTime;
import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberCache;
import ru.ryabtsev.antifraud.conditional.actions.Condition;
import ru.ryabtsev.antifraud.quarantine.QuarantinedEntity;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.traits.DestinationCardNumberContainer;

public class CardNumberPresentsInQuarantineForLongTime implements Condition {

    private final PropertyAccessor<Transaction, DestinationCardNumberContainer, String> cardNumberAccessor;

    private final QuarantinedCardNumberCache quarantinedCardNumberCache;

    private final Duration threshold;

    public CardNumberPresentsInQuarantineForLongTime(
            final PropertyAccessor<Transaction, DestinationCardNumberContainer, String> cardNumberAccessor,
            final QuarantinedCardNumberCache quarantinedCardNumberCache,
            final Duration threshold) {
        this.cardNumberAccessor = cardNumberAccessor;
        this.quarantinedCardNumberCache = quarantinedCardNumberCache;
        this.threshold = threshold;
    }

    @Override
    public boolean isFulfilled() {
        return quarantinedCardNumberCache.findQuarantinedCardNumber(cardNumberAccessor.get())
                .map(QuarantinedEntity::getStartDateTime)
                .map(startDate -> startDate.plus(threshold).isBefore(LocalDateTime.now()))
                .orElse(false);
    }
}
