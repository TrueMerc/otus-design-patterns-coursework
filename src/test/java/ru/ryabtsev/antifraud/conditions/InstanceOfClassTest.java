package ru.ryabtsev.antifraud.conditions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.ryabtsev.antifraud.conditional.actions.Condition;
import ru.ryabtsev.antifraud.conditional.actions.InstanceOfClass;

class InstanceOfClassTest {

    @Test
    void shouldBeInstance() {
        // Given
        final Condition listIsInstanceOfCollection = new InstanceOfClass(List.of(), Collection.class);
        // When
        // There is nothing to do here.

        //
        assertTrue(listIsInstanceOfCollection.isFulfilled());
    }

}