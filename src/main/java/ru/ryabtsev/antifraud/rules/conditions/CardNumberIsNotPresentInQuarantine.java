package ru.ryabtsev.antifraud.rules.conditions;

import ru.ryabtsev.antifraud.caches.QuarantinedCardNumberCache;
import ru.ryabtsev.antifraud.conditional.actions.Condition;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.traits.DestinationCardNumberContainer;

public class CardNumberIsNotPresentInQuarantine implements Condition {

    private final PropertyAccessor<Transaction, DestinationCardNumberContainer, String> cardNumberAccessor;

    private final QuarantinedCardNumberCache quarantinedCardNumberCache;

    public CardNumberIsNotPresentInQuarantine(
            final PropertyAccessor<Transaction, DestinationCardNumberContainer, String> cardNumberAccessor,
            final QuarantinedCardNumberCache quarantinedCardNumberCache) {
        this.cardNumberAccessor = cardNumberAccessor;
        this.quarantinedCardNumberCache = quarantinedCardNumberCache;
    }

    @Override
    public boolean isFulfilled() {
        return quarantinedCardNumberCache.findQuarantinedCardNumber(cardNumberAccessor.get()).isEmpty();
    }
}
