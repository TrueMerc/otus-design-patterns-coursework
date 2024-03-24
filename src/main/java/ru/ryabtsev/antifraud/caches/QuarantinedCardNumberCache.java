package ru.ryabtsev.antifraud.caches;

import java.util.Optional;
import ru.ryabtsev.antifraud.quarantine.QuarantinedEntity;

public interface QuarantinedCardNumberCache {

    Optional<QuarantinedEntity<String>> findQuarantinedCardNumber(final String cardNumber);
}
