package ru.ryabtsev.antifraud.transactions.rbs;

import lombok.Builder;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.traits.ExternalIdContainer;
import ru.ryabtsev.antifraud.transactions.traits.Identifiable;
import ru.ryabtsev.antifraud.transactions.traits.PayeeTinContainer;

@Builder
public class TransferToExternalSystem
        implements Transaction, Identifiable<Long>, ExternalIdContainer<String>, PayeeTinContainer {

    private final Long id;

    private final String externalId;

    private final String payeeTin;

    private String status;

    private String resolution;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getExternalId() {
        return externalId;
    }

    @Override
    public String getPayeeTin() {
        return payeeTin;
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
