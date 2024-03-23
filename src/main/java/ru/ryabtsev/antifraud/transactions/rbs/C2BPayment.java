package ru.ryabtsev.antifraud.transactions.rbs;

import lombok.Builder;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.traits.Identifiable;
import ru.ryabtsev.antifraud.transactions.traits.PayeeTinContainer;

/**
 * The class that corresponds the 'Customer To Business' type of transaction in UFS system.
 */
@Builder
public class C2BPayment implements Transaction, Identifiable<Long>, PayeeTinContainer {

    private final Long id;

    private final String payeeTin;

    private final String status;

    private final String resolution;

    @Override
    public Long getId() {
        return id;
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
