package ru.ryabtsev.antifraud.transactions.rbs;

import lombok.Builder;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.traits.DestinationCardNumberContainer;
import ru.ryabtsev.antifraud.transactions.traits.ExternalIdContainer;
import ru.ryabtsev.antifraud.transactions.traits.Identifiable;

@Builder(setterPrefix = "with")
public class TransferToAnotherCard
        implements Transaction, Identifiable<Long>, ExternalIdContainer<Long>, DestinationCardNumberContainer {

    private Long id;

    private Long externalId;

    private String destinationCardNumber;

    private String status;

    private String resolution;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getExternalId() {
        return externalId;
    }

    @Override
    public String destinationCardNumber() {
        return destinationCardNumber;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getResolution() {
        return resolution;
    }
}
