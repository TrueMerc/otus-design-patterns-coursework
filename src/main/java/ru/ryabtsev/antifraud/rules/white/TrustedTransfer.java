package ru.ryabtsev.antifraud.rules.white;

import java.util.List;
import ru.ryabtsev.antifraud.caches.TrustedCardNumberCache;
import ru.ryabtsev.antifraud.conditional.actions.ConditionalActionWithAlternative;
import ru.ryabtsev.antifraud.conditional.actions.ConditionalActions;
import ru.ryabtsev.antifraud.conditional.actions.DefaultConditionalAction;
import ru.ryabtsev.antifraud.rules.RuleConfiguration;
import ru.ryabtsev.antifraud.rules.actions.RuleIsAppliedGeneration;
import ru.ryabtsev.antifraud.rules.actions.RuleIsNotAppliedGeneration;
import ru.ryabtsev.antifraud.rules.actions.UnsupportedTransactionTypeGeneration;
import ru.ryabtsev.antifraud.rules.condiitons.InstanceOfClass;
import ru.ryabtsev.antifraud.rules.condiitons.PresenceInContainer;
import ru.ryabtsev.antifraud.rules.results.RuleIsApplied;
import ru.ryabtsev.antifraud.transactions.ProcessableTransaction;
import ru.ryabtsev.antifraud.transactions.Transaction;
import ru.ryabtsev.antifraud.transactions.traits.DestinationCardNumberContainer;

public class TrustedTransfer implements WhiteRule {

    private static final String SUCCESSFUL_EXECUTION_MESSAGE = "Параметры транзакции найдены в списке разрешённых";

    private static final String UNSUCCESSFUL_EXECUTION_MESSAGE = "Параметры транзакции не найдены в списке разрешённых";

    private final RuleConfiguration ruleConfiguration;

    private final TrustedCardNumberCache trustedCardNumberCache;

    public TrustedTransfer(
            final RuleConfiguration ruleConfiguration, final TrustedCardNumberCache trustedCardNumberCache) {
        this.ruleConfiguration = ruleConfiguration;
        this.trustedCardNumberCache = trustedCardNumberCache;
    }

    @Override
    public ProcessableTransaction applyTo(final Transaction transaction) {
        final var defaultAction = new UnsupportedTransactionTypeGeneration(this, ruleConfiguration);
        final var presenceInContainer = new PresenceInContainer<>(
                transaction, DestinationCardNumberContainer::getDestinationCardNumber, trustedCardNumberCache
        );
        final var parameterIsNotPresentInWhiteList = new RuleIsNotAppliedGeneration(
                this, ruleConfiguration, UNSUCCESSFUL_EXECUTION_MESSAGE
        );
        final var instanceOfClass = new InstanceOfClass(transaction, DestinationCardNumberContainer.class);
        final var conditionalAcceptance = new ConditionalActionWithAlternative<>(
                presenceInContainer,
                new RuleIsAppliedGeneration(this, ruleConfiguration, SUCCESSFUL_EXECUTION_MESSAGE),
                parameterIsNotPresentInWhiteList
        );
        final var conditionalActions = new ConditionalActions<>(
                List.of(new DefaultConditionalAction<>(instanceOfClass, conditionalAcceptance)),
                defaultAction
        );
        final var ruleExecutionResult = conditionalActions.execute();
        return RuleIsApplied.STATUS.equals(ruleExecutionResult.getStatus())
                ? ProcessableTransaction.ofFinal(transaction, ruleExecutionResult)
                : ProcessableTransaction.ofPartial(transaction, ruleExecutionResult);
    }
}
