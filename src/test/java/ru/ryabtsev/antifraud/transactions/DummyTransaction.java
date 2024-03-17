package ru.ryabtsev.antifraud.transactions;

class DummyTransaction implements Transaction {

    private final String status;
    private final String resolution;

    public DummyTransaction(final String status, final String resolution) {
        this.status = status;
        this.resolution = resolution;
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
