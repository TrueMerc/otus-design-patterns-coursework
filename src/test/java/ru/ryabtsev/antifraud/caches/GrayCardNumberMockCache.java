package ru.ryabtsev.antifraud.caches;

import java.util.List;

public class GrayCardNumberMockCache implements GrayCardNumberCache {

    private final List<String> cardNumbers;

    public GrayCardNumberMockCache(final List<String> cardNumbers) {
        this.cardNumbers = cardNumbers;
    }

    @Override
    public boolean contains(final String cardNumber) {
        return cardNumbers.contains(cardNumber);
    }
}
