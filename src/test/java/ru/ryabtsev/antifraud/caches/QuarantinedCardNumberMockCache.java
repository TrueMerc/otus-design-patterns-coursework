package ru.ryabtsev.antifraud.caches;

import java.util.List;
import java.util.Optional;
import ru.ryabtsev.antifraud.quarantine.QuarantinedEntity;

public class QuarantinedCardNumberMockCache implements QuarantinedCardNumberCache {

    private final List<QuarantinedEntity<String>> quarantinedCardNumbers;

    public QuarantinedCardNumberMockCache(List<QuarantinedEntity<String>> quarantinedCardNumbers) {
        this.quarantinedCardNumbers = quarantinedCardNumbers;
    }

    @Override
    public Optional<QuarantinedEntity<String>> findQuarantinedCardNumber(final String cardNumber) {
        return quarantinedCardNumbers.stream()
                .filter(quarantinedCardNumber -> quarantinedCardNumber.getValue().equals(cardNumber))
                .findFirst();
    }
}
