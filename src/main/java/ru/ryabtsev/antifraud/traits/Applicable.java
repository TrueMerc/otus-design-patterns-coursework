package ru.ryabtsev.antifraud.traits;

public interface Applicable<T, U> {

    T applyTo(T target);
}
