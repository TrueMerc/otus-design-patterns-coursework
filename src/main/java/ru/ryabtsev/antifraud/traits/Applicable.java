package ru.ryabtsev.antifraud.traits;

public interface Applicable<T, U> {

    U applyTo(T target);
}
