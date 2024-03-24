package ru.ryabtsev.antifraud.quarantine;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class QuarantinedEntity<T> {

    private final T value;

    private final LocalDateTime startDateTime;

    private final LocalDateTime expirationDateTime;

    public QuarantinedEntity(final T value, final Duration duration) {
        this(value, LocalDateTime.now(), duration);
    }

    public QuarantinedEntity(final T value, final LocalDateTime startDateTime, final Duration duration) {
        this.value = value;
        this.startDateTime = startDateTime;
        this.expirationDateTime = startDateTime.plus(duration);
    }
}
