package ru.ryabtsev.antifraud.transactions.ufs;

import lombok.Builder;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.traits.ExternalIdContainer;
import ru.ryabtsev.antifraud.transactions.traits.Identifiable;
import ru.ryabtsev.antifraud.transactions.traits.PayeeTinContainer;

/**
 * The class that corresponds 'Money Transfer' type of transaction in UFS system.
 */
@Builder(setterPrefix = "with")
public class MoneyTransfer
        implements Transaction, Identifiable<Long>, ExternalIdContainer<String>, PayeeTinContainer {

    private final Long id;

    private final String externalId;

    private final String payeeTin;

    private final String status;

    private final String resolution;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getExternalId() {
        return null;
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
