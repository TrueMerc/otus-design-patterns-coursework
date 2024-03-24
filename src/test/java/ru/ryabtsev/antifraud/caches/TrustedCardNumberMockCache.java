package ru.ryabtsev.antifraud.caches;

import java.util.List;

public class TrustedCardNumberMockCache implements TrustedCardNumberCache {

    private final List<String> cardNumbers;

    public TrustedCardNumberMockCache(final List<String> cardNumbers) {
        this.cardNumbers = cardNumbers;
    }

    @Override
    public boolean contains(final String cardNumber) {
        return cardNumbers.contains(cardNumber);
    }
}
