package ru.ryabtsev.antifraud.conditional.actions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.ryabtsev.antifraud.rules.conditions.InstanceOfClass;

class DefaultConditionalActionTest {


    @Test
    void shouldBeExecutable() {
        // Given
        final ConditionalAction<Integer> truthBasedActionOne = new DefaultConditionalAction<>(
                new InstanceOfClass(List.of(), Collection.class),
                () -> Integer.valueOf(5),
                ExecutionCondition.FULFILLMENT
        );
        final ConditionalAction<Integer> truthBasedActionTwo = new DefaultConditionalAction<>(
                new InstanceOfClass(List.of(), Collection.class),
                () -> Integer.valueOf(5),
                ExecutionCondition.VIOLATION
        );
        final ConditionalAction<Integer> falsityBasedActionOne = new DefaultConditionalAction<>(
                new InstanceOfClass(Map.of(), Collection.class),
                () -> Integer.valueOf(1),
                ExecutionCondition.VIOLATION
        );
        final ConditionalAction<Integer> falsityBasedActionTwo = new DefaultConditionalAction<>(
                new InstanceOfClass(Map.of(), Collection.class),
                () -> Integer.valueOf(1),
                ExecutionCondition.FULFILLMENT
        );

        // When
        // There is nothing to do here.

        // Then
        assertTrue(truthBasedActionOne.canBeExecuted());
        assertFalse(truthBasedActionTwo.canBeExecuted());
        assertTrue(falsityBasedActionOne.canBeExecuted());
        assertFalse(falsityBasedActionTwo.canBeExecuted());

        assertEquals(5, truthBasedActionOne.getAction().execute().intValue());
    }
}