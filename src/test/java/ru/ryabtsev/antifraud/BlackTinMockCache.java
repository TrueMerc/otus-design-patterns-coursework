package ru.ryabtsev.antifraud;

import java.util.List;
import ru.ryabtsev.antifraud.caches.BlackTinCache;

public class BlackTinMockCache implements BlackTinCache {

    private final List<String> tins;

    public BlackTinMockCache(final List<String> tins) {
        this.tins = tins;
    }

    @Override
    public boolean contains(String tin) {
        return tins.contains(tin);
    }
}
