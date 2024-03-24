package ru.ryabtsev.antifraud.transactions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import ru.ryabtsev.antifraud.rules.results.RuleExecutionResult;
import ru.ryabtsev.antifraud.traits.Processable;

/**
 *
 */
public abstract class ProcessableTransaction implements Transaction, Processable {

    private static final int RULE_EXECUTION_RESULTS_DEFAULT_CAPACITY = 50;

    private final Transaction transaction;

    private final List<RuleExecutionResult> ruleExecutionResults;

    ProcessableTransaction(final Transaction transaction, final RuleExecutionResult result) {
        this.transaction = transaction;
        this.ruleExecutionResults = new ArrayList<>(RULE_EXECUTION_RESULTS_DEFAULT_CAPACITY);
        addResult(result);
    }

    ProcessableTransaction(final Transaction transaction) {
        this.transaction = transaction;
        this.ruleExecutionResults = new ArrayList<>(RULE_EXECUTION_RESULTS_DEFAULT_CAPACITY);
    }

    ProcessableTransaction(final ProcessableTransaction processableTransaction) {
        transaction = processableTransaction.getTransaction();
        ruleExecutionResults = processableTransaction.ruleExecutionResults;
    }

    public static ProcessableTransaction ofFinal(final Transaction transaction, final RuleExecutionResult result) {
        return createOrUpdateProcessedTransaction(transaction, result, ProcessedTransaction.class);
    }

    private static ProcessableTransaction createOrUpdateProcessedTransaction(
            final Transaction transaction,
            final RuleExecutionResult ruleExecutionResult,
            final Class<? extends ProcessableTransaction> implementationClass) {
        if (transaction instanceof ProcessableTransaction processableTransaction) {
            if (processableTransaction.isProcessed()) {
                throw new IllegalArgumentException("Transaction " + transaction + " is already processed");
            } else {
                final ProcessableTransaction result = processableTransaction.getClass().equals(implementationClass)
                        ? processableTransaction
                        : createCopyOfAnotherType(processableTransaction, implementationClass);
                if (ruleExecutionResult != null) {
                    processableTransaction.addResult(ruleExecutionResult);
                }
                return result;
            }
        } else {
            return createProcessedTransaction(transaction, ruleExecutionResult, implementationClass);
        }
    }

    private static ProcessableTransaction createProcessedTransaction(
            final Transaction transaction,
            final RuleExecutionResult ruleExecutionResult,
            final Class<? extends ProcessableTransaction> implementationClass) {
        try {
            final boolean isResultPresent = ruleExecutionResult != null;
            final Constructor<? extends ProcessableTransaction> constructor = isResultPresent
                    ? implementationClass.getDeclaredConstructor(Transaction.class, RuleExecutionResult.class)
                    : implementationClass.getDeclaredConstructor(Transaction.class);
            return isResultPresent
                    ? constructor.newInstance(transaction, ruleExecutionResult)
                    : constructor.newInstance(transaction);
        } catch (final NoSuchMethodException e) {
            throw new IllegalStateException("Can't find appropriate constructor in " + implementationClass, e);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Can't create instance of " + implementationClass, e);
        }
    }

    private static ProcessableTransaction createCopyOfAnotherType(
            final ProcessableTransaction processableTransaction,
//            final RuleExecutionResult ruleExecutionResult,
            final Class<? extends ProcessableTransaction> implementationClass) {
        try {
            final Constructor<? extends ProcessableTransaction> constructor =
                    implementationClass.getDeclaredConstructor(ProcessableTransaction.class);
            final ProcessableTransaction newTransaction = constructor.newInstance(processableTransaction);
            return newTransaction;
        } catch (final NoSuchMethodException e) {
            throw new IllegalStateException("Can't find appropriate constructor in " + implementationClass, e);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Can't create instance of " + implementationClass, e);
        }
    }

    public static ProcessableTransaction ofFinal(final Transaction transaction) {
        return createOrUpdateProcessedTransaction(transaction, null, ProcessedTransaction.class);
    }

    public static ProcessableTransaction ofPartial(final Transaction transaction, final RuleExecutionResult result) {
        return createOrUpdateProcessedTransaction(transaction, result, PartiallyProcessedTransaction.class);
    }

    public static ProcessableTransaction ofPartial(final Transaction transaction) {
        return createOrUpdateProcessedTransaction(transaction, null, PartiallyProcessedTransaction.class);
    }

    private void addResult(final RuleExecutionResult result) {
        ruleExecutionResults.add(result);
    }

    @Override
    public abstract boolean isProcessed();

    @Override
    public String getStatus() {
        return transaction.getStatus();
    }

    @Override
    public String getResolution() {
        return transaction.getResolution();
    }

    public List<RuleExecutionResult> getRuleExecutionResults() {
        return List.copyOf(ruleExecutionResults);
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
